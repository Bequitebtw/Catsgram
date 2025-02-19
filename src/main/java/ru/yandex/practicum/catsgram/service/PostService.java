package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    private final UserService userService;

    /* Выводит последние 10 постов с конца, в принципе можно убрать этот метод, но мне кажется он быстрее
        чем выводить 10 элементов с конца через finWithParams
     */
    public Collection<Post> findFreshPosts() {
        return posts.values().stream().toList().subList(Math.max(0, posts.size() - 10), posts.size());
    }

    public Collection<Post> findWithParams(String sort, String size, String from) {
        ArrayList<Post> listWithParams = new ArrayList<>();
        int integerSize = Integer.parseInt(size);
        int integerFrom = Integer.parseInt(from);

        //чтобы не выводился пустой массив при неправильном вводе
        if (integerFrom <= 0) {
            integerFrom = 1;
        }
        if (integerSize <= 0) {
            integerSize = 10;
        }
        //чтобы цикл не вышел за границы массива
        if (posts.size() < integerSize) {
            integerSize = posts.size();
        }
        for (long x = integerFrom; x < integerFrom + integerSize; x++) {
            /* На случай если будет удаление постов, то поста с айди по порядку может не быть. Для вывода точного количества
              элементов не отталкиваясь от id, можно написать дополнительную проверку, чтобы поиск не выходил
              за рамки массива и добавлял integerFrom если post не найден.
            */
            if (posts.get(x) != null) {
                listWithParams.add(posts.get(x));
            }
        }
        if (sort.equals("asc")) {
            return listWithParams.stream().sorted(Comparator.comparing(Post::getPostDate)).toList();
        }
        return listWithParams.stream().sorted(Comparator.comparing(Post::getPostDate).reversed()).toList();
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    public Post create(Post post) {
        userService.findUserById(post.getAuthorId());
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post findPostById(Long id) {
        if (posts.containsKey(id)) {
            return posts.get(id);
        }
        throw new ConditionsNotMetException("Нет поста с id: " + id);
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
