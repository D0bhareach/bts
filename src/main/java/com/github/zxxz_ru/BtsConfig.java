package com.github.zxxz_ru;
import com.github.zxxz_ru.entity.User;
import com.github.zxxz_ru.storage.dao.UserRepository;
import com.github.zxxz_ru.storage.file.EntityMode;
import com.github.zxxz_ru.storage.file.FileSystemRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;

@Configuration
@ComponentScan
public class BtsConfig {
    @Bean
    CrudRepository userRepository(){
        return new FileSystemRepository<User>(EntityMode.USER);
    }
}
