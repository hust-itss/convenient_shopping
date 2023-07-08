package com.example.convenientshoppingapp.serviceTest;

import com.example.convenientshoppingapp.entity.Dish;
import com.example.convenientshoppingapp.entity.Recipe;
import com.example.convenientshoppingapp.repository.DishRepository;
import com.example.convenientshoppingapp.repository.FavoriteRespository;
import com.example.convenientshoppingapp.repository.RecipeRepository;
import com.example.convenientshoppingapp.service.impl.RecipeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private FavoriteRespository favoriteRespository;

    @InjectMocks
    private RecipeService recipeService;

//    @Test
//    public void testAddFavorite_Success() {
//        // Arrange
//        Long recipeId = 1L;
//        User user = new User();
//        user.setId(123L);
//
//        // Mock the recipe repository
//        Recipe recipe = new Recipe();
//        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
//
//        // Mock the favorite repository
//        when(favoriteRespository.existsByUserIdAndRecipeId(anyLong(), anyLong())).thenReturn(false);
//
//        // Thiết lập thông tin xác thực cho đối tượng User giả lập
//        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
//
//        // Thiết lập thông tin xác thực vào SecurityContextHolder
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Act
//        ResponseEntity<ResponseObject> response = recipeService.addFavorite(recipeId);
//
//        // Assert
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//        Assertions.assertEquals("success", response.getBody().getStatus());
//        Assertions.assertEquals("Thêm công thức yêu thích thành công", response.getBody().getMessage());
//        Assertions.assertNull(response.getBody().getData());
//
//        // Verify the save method is called
//        verify(favoriteRespository, times(1)).save(any(Favorite.class));
//    }
//
//    @Test
//    public void addFavorite_RecipeNotFound() {
//        // Arrange
//        Long recipeId = 1L;
//
//        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());
//
//        // Act
//        ResponseEntity<ResponseObject> response = recipeService.addFavorite(recipeId);
//
//        // Assert
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("error", response.getBody().getStatus());
//        assertEquals("Công thức nấu ăn này không tồn tại", response.getBody().getMessage());
//        assertEquals("", response.getBody().getData());
//
//        // Verify
//        verify(recipeRepository).findById(recipeId);
//        verify(favoriteRespository, never()).existsByUserIdAndRecipeId(anyLong(), anyLong());
//        verify(favoriteRespository, never()).save(any(Favorite.class));
//    }
//
//    @Test
//    public void addFavorite_RecipeAlreadyInFavorites() {
//        // Arrange
//        Long recipeId = 1L;
//        User user = new User();
//        user.setId(123L);
//
//        Recipe recipe = new Recipe();
//        recipe.setId(recipeId);
//
//        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
//        when(favoriteRespository.existsByUserIdAndRecipeId(user.getId(), recipeId)).thenReturn(true);
//
//        // Thiết lập thông tin xác thực cho đối tượng User giả lập
//        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
//
//        // Thiết lập thông tin xác thực vào SecurityContextHolder
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Act
//        ResponseEntity<ResponseObject> response = recipeService.addFavorite(recipeId);
//
//        // Assert
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("error", response.getBody().getStatus());
//        assertEquals("Công thức yêu thích đã có trong tài khoản của bạn", response.getBody().getMessage());
//        assertEquals("", response.getBody().getData());
//
//        // Verify
//        verify(recipeRepository).findById(recipeId);
//        verify(favoriteRespository).existsByUserIdAndRecipeId(user.getId(), recipeId);
//        verify(favoriteRespository, never()).save(any(Favorite.class));
//    }

    @Test
    public void save_Successful() {
        // Arrange
        Long dishId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setDishId(dishId);

        Dish dish = new Dish();
        dish.setId(dishId);

        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));
        when(recipeRepository.save(recipe)).thenReturn(recipe);

        // Act
        Recipe savedRecipe = recipeService.save(recipe);

        // Assert
        assertNotNull(savedRecipe);
        assertEquals(dishId, savedRecipe.getDishId());
        assertEquals(dish, savedRecipe.getDish());

        // Verify
        verify(dishRepository).findById(dishId);
        verify(recipeRepository).save(recipe);
    }

    @Test
    public void save_DishNotFound() {
        // Arrange
        Long dishId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setDishId(dishId);

        when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> recipeService.save(recipe));

        // Verify
        verify(dishRepository).findById(dishId);
        verify(recipeRepository, never()).save(recipe);
    }

    @Test
    public void update_Successful() {
        // Arrange
        Long recipeId = 1L;
        Long dishId = 2L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setDishId(dishId);

        Recipe oldRecipe = new Recipe();
        oldRecipe.setId(recipeId);
        oldRecipe.setDishId(3L);

        Dish dish = new Dish();
        dish.setId(dishId);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(oldRecipe));
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));
        when(recipeRepository.save(oldRecipe)).thenReturn(oldRecipe);

        // Act
        Recipe updatedRecipe = recipeService.update(recipe);

        // Assert
        assertNotNull(updatedRecipe);
        assertEquals(dishId, updatedRecipe.getDishId());
        assertEquals(dish, updatedRecipe.getDish());

        // Verify
        verify(recipeRepository).findById(recipeId);
        verify(dishRepository).findById(dishId);
        verify(recipeRepository).save(oldRecipe);
    }

    @Test
    public void update_RecipeNotFound() {
        // Arrange
        Long recipeId = 1L;
        Long dishId = 2L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setDishId(dishId);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> recipeService.update(recipe));

        // Verify
        verify(recipeRepository).findById(recipeId);
        verify(dishRepository, never()).findById(anyLong());
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    public void update_DishNotFound() {
        // Arrange
        Long recipeId = 1L;
        Long dishId = 2L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setDishId(dishId);

        Recipe oldRecipe = new Recipe();
        oldRecipe.setId(recipeId);
        oldRecipe.setDishId(3L);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(oldRecipe));
        when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> recipeService.update(recipe));

        // Verify
        verify(recipeRepository).findById(recipeId);
        verify(dishRepository).findById(dishId);
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    public void getAllRecipe() {
        // Arrange
        int page = 0;
        int size = 10;
        Pageable paging = PageRequest.of(page, size, Sort.by("id").ascending());

        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);

        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);

        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        Page<Recipe> recipePage = new PageImpl<>(recipes, paging, 2);

        when(recipeRepository.findAll(paging)).thenReturn(recipePage);

        // Act
        Map<String, Object> response = recipeService.getAllRecipe(page, size);

        // Assert
        assertNotNull(response);
        assertTrue(response.containsKey("recipes"));
        assertTrue(response.containsKey("currentPage"));
        assertTrue(response.containsKey("totalItems"));
        assertTrue(response.containsKey("totalPages"));

        List<Recipe> returnedRecipes = (List<Recipe>) response.get("recipes");
        assertEquals(recipes, returnedRecipes);
        assertEquals(page, response.get("currentPage"));
        assertEquals(2L, response.get("totalItems"));
        assertEquals(1, response.get("totalPages"));

        // Verify
        verify(recipeRepository).findAll(paging);
    }

    @Test
    public void deleteRecipeById() {
        // Arrange
        Long[] ids = {1L, 2L};

        // Act
        recipeService.deleteRecipeById(ids);

        // Verify
        for (Long id : ids) {
            verify(recipeRepository).deleteRecipeById(id);
        }
    }
}
