package com.geostar.geostack.git_branch_manager.config;

import com.geostar.geostack.git_branch_manager.common.YamlWriter;
import com.geostar.geostack.git_branch_manager.pojo.GitProject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties
public class GitRepositoryConfig {

    private String workHome;

    private String modulesHome;

    private final List<GitProject> gitProjects = new ArrayList<>();

    private String gitUsername;

    private String gitPassword;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWorkHome() {
        return workHome;
    }

    public void setWorkHome(String workHome) {
        this.workHome = workHome;
    }

    public String getModulesHome() {
        return modulesHome;
    }

    public void setModulesHome(String modulesHome) {
        this.modulesHome = modulesHome;
    }

    public List<GitProject> getProjects() {
        return gitProjects;
    }

    public void setGitProjects(List<String> projects, YamlWriter yamlWriter) throws Exception {
        for (String project : projects) {
            setGitProjects(project);
            yamlWriter.modifyGitProjectConfig(project);
        }
    }

    public void setGitProjects(String project) {
        GitProject gitProject = new GitProject();
        String projectName = project.substring(project.lastIndexOf("/") + 1, project.length() - ".git".length());
        gitProject.setName(projectName);
        gitProject.setRemoteUrl(project);
        gitProjects.add(gitProject);
    }

    public void setProjects(List<String> projects) {
        gitProjects.clear();
        for (String project : projects) {
            GitProject gitProject = new GitProject();
            String projectName = project.substring(project.lastIndexOf("/") + 1, project.length() - ".git".length());
            gitProject.setName(projectName);
            gitProject.setRemoteUrl(project);
            gitProjects.add(gitProject);
        }
    }

    public String getGitUsername() {
        return gitUsername;
    }

    public void setGitUsername(String gitUsername) {
        this.gitUsername = gitUsername;
    }

    public String getGitPassword() {
        return gitPassword;
    }

    public void setGitPassword(String gitPassword) {
        this.gitPassword = gitPassword;
    }

    public String getCodingToken(String defaultToken){
        return StringUtils.hasLength(token) ? "token ".concat(token) : "token ".concat(defaultToken);
    }
}
