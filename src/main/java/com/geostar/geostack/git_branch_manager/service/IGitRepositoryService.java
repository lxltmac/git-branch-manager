package com.geostar.geostack.git_branch_manager.service;

import com.geostar.geostack.git_branch_manager.common.Page;
import com.geostar.geostack.git_branch_manager.pojo.GitLog;
import com.geostar.geostack.git_branch_manager.pojo.GitProject;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.List;

public interface IGitRepositoryService {

    List<GitProject> getAllGitProject();

    /**
     * 克隆或者拉取项目
     *
     * @param gitProject
     * @return
     */
    boolean cloneOrPull(GitProject gitProject) throws IOException, GitAPIException;

    /**
     * 更新项目信息
     *
     * @param gitProject
     */
    boolean updateGitProjectInfo(GitProject gitProject) throws IOException, GitAPIException;

    /**
     * 创建分支
     *
     * @param gitProject
     * @param branchName
     */
    boolean createBranch(GitProject gitProject, String branchName) throws IOException, GitAPIException;

    /**
     * 切换分支
     *
     * @param gitProject
     * @param branchName
     */
    boolean switchBranch(GitProject gitProject, String branchName) throws GitAPIException, IOException;

    /**
     * 推送代码
     *
     * @param gitProject
     * @return
     */
    boolean push(GitProject gitProject, String message) throws IOException, GitAPIException;

    /**
     * 删除当前分支分支
     *
     * @param gitProject
     * @return
     */
    boolean deleteBranch(GitProject gitProject) throws IOException, GitAPIException;

    /**
     * 获取所有项目的分支交集
     *
     * @param projects
     * @return
     */
    List<String> getBranchIntersect(List<GitProject> projects);

    /**
     * 创建标签
     *
     * @param gitProject
     * @param tagName
     * @param tagLog
     * @return
     */
    boolean createTag(GitProject gitProject, String tagName, String tagLog) throws IOException, GitAPIException;

    /**
     * 从某标签检出代码到某新分支
     *
     * @param gitProject
     * @param tagName
     * @param branchName
     */
    void createBranchByTag(GitProject gitProject, String tagName, String branchName) throws IOException, GitAPIException;

    /**
     * 获取所有项目的标签交集
     *
     * @param projects
     * @return
     */
    List<String> getTagIntersect(List<GitProject> projects);

    /**
     * 获取文件内容
     *
     * @param gitProject
     * @param fileName
     * @return
     */
    String getFileContent(GitProject gitProject, String fileName) throws IOException;

    /**
     * 删除标签
     *
     * @param gitProject
     * @param tagName
     * @return
     */
    boolean deleteTag(GitProject gitProject, String tagName) throws IOException, GitAPIException;

    /**
     * 合并分支，将被合并分支的修改并入当前工作分支
     *
     * @param gitProject
     * @param currWorkBranch 当前工作分支
     * @param sourceBranch   被合并的分支
     * @return
     */
    boolean mergeBranch(GitProject gitProject, String currWorkBranch, String sourceBranch, String message) throws IOException, GitAPIException;


    /**
     * 合并分支，将被合并分支的修改并入当前工作分支
     *
     * @param gitProject
     * @param currWorkBranch 当前工作分支
     * @param sourceBranch   被合并的分支
     * @return
     */
    boolean mergeBranchReq(GitProject gitProject, String currWorkBranch, String sourceBranch, String message, String content) throws IOException;

    /**
     * 获取日志
     *
     * @param page
     * @param username
     * @param projectName
     */
    void getCommitLogs(Page<GitLog> page, String username, String projectName) throws IOException, GitAPIException;

}
