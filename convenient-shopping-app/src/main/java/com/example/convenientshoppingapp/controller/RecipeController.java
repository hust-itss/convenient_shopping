package com.example.convenientshoppingapp.controller;

import com.example.convenientshoppingapp.dto.AddFavoriteRecipeRequest;
import com.example.convenientshoppingapp.dto.recipe.CreateRecipeRequest;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    @PostMapping("/add_favorite")
    public ResponseEntity<ResponseObject> addFavorite(@Valid @RequestBody AddFavoriteRecipeRequest request) {
        return recipeService.addFavorite(request.getRecipeId());
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> save(@RequestBody @Valid CreateRecipeRequest request) {
        return recipeService.save(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable("id") Long id,@RequestBody @Valid CreateRecipeRequest recipe) {
        return recipeService.update(recipe, id);
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "", name = "name") String name,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "0", name = "myRecipe") int myRecipe
    ) {
        return recipeService.getAllRecipe(name, page,size, myRecipe);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable("id") Long id) {
        return recipeService.deleteRecipeById(id);
    }
   
}
