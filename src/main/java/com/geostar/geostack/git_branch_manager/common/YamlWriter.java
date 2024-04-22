package com.geostar.geostack.git_branch_manager.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.geostar.geostack.git_branch_manager.config.GitRepositoryConfig;
import com.geostar.geostack.git_branch_manager.pojo.GitProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class YamlWriter {

    private static final Logger logger = LoggerFactory.getLogger(YamlWriter.class);

    @Autowired
    private Environment environment;

    public void modifyGitProjectConfig(String newProjectUrl) throws IOException, URISyntaxException {
        String filePath = environment.getProperty("git-save-location");
//        String filePath = Objects.requireNonNull(YamlWriter.class.getResource("/application-git.yml")).getFile();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                // 关键配置：禁用字符串自动引号
                .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                .enable(YAMLGenerator.Feature.INDENT_ARRAYS));

        GitRepositoryConfig appConfig = mapper.readValue(new File(filePath), GitRepositoryConfig.class);

        List<String> gitProjects = appConfig.getProjects().stream()
                .map(GitProject::getRemoteUrl)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        gitProjects.add(newProjectUrl);

        Map<String, List<String>> newProjects = new HashMap<>();
        newProjects.put("projects", gitProjects);

        mapper.writeValue(new File(filePath), newProjects);
        System.out.println("更新 application-git 文件成功"); // 替换为您的日志记录方式
    }
}
