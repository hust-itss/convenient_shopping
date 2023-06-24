package com.example.convenientshoppingapp.controller;

import com.example.convenientshoppingapp.dto.AddFavoriteRecipeRequest;
import com.example.convenientshoppingapp.entity.Recipe;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.service.impl.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    @PostMapping("/add_favorite")
    public ResponseEntity<ResponseObject> addFavorite(@Valid @RequestBody AddFavoriteRecipeRequest request) {
        return recipeService.addFavorite(request.getRecipeId());
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> save(@RequestBody @Valid Recipe recipe) {
        recipeService.save(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("success", "Insert dữ liệu thành công", recipe));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable("id") Long id,@RequestBody @Valid Recipe recipe) {
        recipe.setId(id);
        recipeService.save(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("success", "Cập nhật dữ liệu thành công", ""));
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "0", name = "page") int page
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Lấy dữ liệu thành công", recipeService.getAllRecipe(page,size)));

    }

    @DeleteMapping("")
    public ResponseEntity<ResponseObject> delete(@RequestBody Long[] ids) {
        recipeService.deleteRecipeById(ids);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("success", "Xóa dữ liệu thành công", ""));
    }
   
}
