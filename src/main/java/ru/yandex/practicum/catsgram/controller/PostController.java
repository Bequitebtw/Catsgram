package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    //Не задана сортировка sort - сортируется по возрастанию
    //Не задан параметр size или size <= 0 - выводится 10 элементов
    //Не задан параметр from или from <= 0 - вывод начинается с единицы
    @GetMapping()
    public Collection<Post> findAll(@RequestParam(defaultValue = "asc") String sort,
                                    @RequestParam(defaultValue = "0") String size,
                                    @RequestParam(defaultValue = "0") String from) {
        if (sort.equals("asc") && size.equals("0") && from.equals("0")) {
            return postService.findFreshPosts();
        }
        return postService.findWithParams(sort, size, from);
    }

    @GetMapping(value = {"/{id}"})
    public Post getPostById(@PathVariable Long id) {
        return postService.findPostById(id);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

}