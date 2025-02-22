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

    /*   Не понял как реализовать, что если нет параметров ищутся последние 10 постов, а если есть, то по параметрам.
        Какие в итоге параметры выставлять в дефолте? пока что сделал так, маловероятно что пользователь выставит такие значения
        поэтому они будут считаться за дефолтные, но я бы сделал отдельный эндпоинт для этого.
        Сейчас фактически все параметры для пользователя являются обязательными, хотя допустим сортировку можно было бы
        сделать по дефолту в asc
        */
    @GetMapping()
    public Collection<Post> findAll(@RequestParam(defaultValue = "sort") String sort,
                                    @RequestParam(defaultValue = "size") String size,
                                    @RequestParam(defaultValue = "from") String from) {
        if (sort.equals("sort") || size.equals("size") || from.equals("from")) {
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