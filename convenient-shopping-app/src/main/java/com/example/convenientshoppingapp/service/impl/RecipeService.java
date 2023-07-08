package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.Utils.UserUtil;
import com.example.convenientshoppingapp.dto.PaginationResponse;
import com.example.convenientshoppingapp.dto.recipe.CreateRecipeRequest;
import com.example.convenientshoppingapp.dto.recipe.RecipeResponse;
import com.example.convenientshoppingapp.entity.*;
import com.example.convenientshoppingapp.entity.auth.User;
import com.example.convenientshoppingapp.repository.DishRepository;
import com.example.convenientshoppingapp.repository.FavoriteRespository;
import com.example.convenientshoppingapp.repository.FoodRepository;
import com.example.convenientshoppingapp.repository.RecipeRepository;
import com.example.convenientshoppingapp.repository.spec.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final DishRepository dishRepository;
    private final FavoriteRespository favoriteRespository;
    private final FoodRepository foodRepository;

    private final ModelMapper modelMapper;

    public ResponseEntity<ResponseObject> addFavorite(Long recipeId) {

        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if(!recipe.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Công thức nấu ăn này không tồn tại", ""));
        }
        Long userId = UserUtil.getCurrentUserId();
        if(favoriteRespository.existsByUserIdAndRecipeId(userId, recipeId)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("error", "Công thức yêu thích đã có trong tài khoản của bạn", ""));
        }

        Favorite favorite = new Favorite().builder().recipeId(recipeId).userId(userId).build();

        favoriteRespository.save(favorite);
        if(favoriteRespository.existsByUserIdAndRecipeId(userId, recipeId)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseObject("success", "Thêm công thức yêu thích thành công", ""));
        }
        return null;
    }

    @Modifying
    public ResponseEntity<ResponseObject> save(CreateRecipeRequest recipeRequest) {
        Recipe recipe = modelMapper.map(recipeRequest, Recipe.class);
        recipe.setFoods(foodRepository.getAllByIdIn(recipeRequest.getFoodIds()));
        Long userId = UserUtil.getCurrentUserId();
        recipe.setUserId(userId);
        recipeRepository.save(recipe);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Thêm công thức yêu thích thành công", ""));
    }

    @Modifying
    public ResponseEntity<ResponseObject> update(CreateRecipeRequest recipeRequest, Long recipeId) {
        Long userId = UserUtil.getCurrentUserId();
        Optional<Recipe> recipeOptional = recipeRepository.findByIdAndAndUserId(recipeId, userId);
        if(!recipeOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Công thức nấu ăn này không tồn tại hoặc không thuộc quyền sở hữu của bạn", ""));
        }

        Recipe recipe = recipeOptional.get();
        recipe.setName(recipe.getName());
        recipe.setDescriptions(recipeRequest.getDescriptions());
        recipe.setPosterLink(recipeRequest.getPosterLink());
        recipe.setFoods(foodRepository.getAllByIdIn(recipeRequest.getFoodIds()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Cập nhật công thức thành công", ""));
    }

    public ResponseEntity<ResponseObject> getAllRecipe(String name, int page, int size, int myRecipe) {
        Specification<Recipe> spec = where(null);
        if(name.length() > 0) {
            MySpecification esName = new MySpecification();
            esName.add(new SearchCriteria("name", name, SearchOperation.MATCH));
            spec = spec.and(esName);
        }

        if(myRecipe == 1) {
            Long userId = UserUtil.getCurrentUserId();
            MySpecification esName = new MySpecification();
            esName.add(new SearchCriteria("userId", userId, SearchOperation.EQUAL));
            spec = spec.and(esName);
        }

        Pageable paging = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<Recipe> pageRecipe;
        pageRecipe = recipeRepository.findAll(spec, paging);
        List<Recipe> recipes = pageRecipe.getContent();
        List<RecipeResponse> recipeResponses = Arrays.asList(modelMapper.map(recipes, RecipeResponse[].class));
        PaginationResponse<RecipeResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setItems(recipeResponses);
        paginationResponse.setCurrentPage(pageRecipe.getNumber());
        paginationResponse.setTotalItem(pageRecipe.getTotalElements());
        paginationResponse.setTotalPage(pageRecipe.getTotalPages());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", paginationResponse));
    }

    @Modifying
    public ResponseEntity<ResponseObject> deleteRecipeById(Long id) {
        Long userId = UserUtil.getCurrentUserId();
        if(!recipeRepository.existsByIdAndUserId(id, userId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Công thức này không tồn tại hoặc không thuộc quyền sở hữu của bạn", null));
        }

        recipeRepository.deleteRecipeById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Xóa công thức thành công", ""));
    }
}
