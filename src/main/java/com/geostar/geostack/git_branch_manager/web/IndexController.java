package com.geostar.geostack.git_branch_manager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.geostar.geostack.git_branch_manager.common.Page;
import com.geostar.geostack.git_branch_manager.common.YamlWriter;
import com.geostar.geostack.git_branch_manager.config.GitRepositoryConfig;
import com.geostar.geostack.git_branch_manager.pojo.GitLog;
import com.geostar.geostack.git_branch_manager.pojo.GitProject;
import com.geostar.geostack.git_branch_manager.pojo.coding.CodingMergersInfo;
import com.geostar.geostack.git_branch_manager.service.IGitRepositoryService;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    /**
     * 首页对应的模板名称
     */
    private static final String INDEX_HTML = "list";
    /**
     * 未提交文件view的URI路径前缀
     */
    private static final String UNTRACKED_FILE_VIEW_PATH_PREFIX = "/untrackedFileView/";
    @Resource
    private IGitRepositoryService gitRepositoryService;
    @Autowired
    private GitRepositoryConfig gitRepositoryConfig;
    @Autowired
    private YamlWriter yamlWriter;

    /**
     * 首页展示
     *
     * @return
     */
    @RequestMapping("/")
    public String index(Model model) {
        logger.info("index进行首页展示");
        list(model);
        return INDEX_HTML;
    }

    @RequestMapping("/login")
    public String login(Model model){
        return "login";
    }

    @RequestMapping("/loginSuccess")
    public String list1(Model model){
        return "loginSuccess";
    }

    @RequestMapping("/listMerge")
    public String listMerge(Model model) {
        logger.info("进入列表展示");
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        if(CollectionUtils.isEmpty(projects)){
            return "list";
        }
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
//                e.printStackTrace();
                logger.error("更新git项目信息失败", e);
            } catch (GitAPIException e) {
//                e.printStackTrace();
                logger.error("更新git项目信息失败", e);
            }
        }
        modelMergeBuild(model, projects, true);
        return "listMerge";
    }

    @RequestMapping("/isLogin")
    @ResponseBody
    public String isLogin(Model model, String username, String password, boolean flag) throws IOException, InterruptedException {
        logger.info("进行登录操作username:{},password:{}", username, password);
        if ((StringUtils.hasLength(username) && StringUtils.hasLength(password))
                && flag) {
            if (!username.equals(gitRepositoryConfig.getGitUsername())
                    || !password.equals(gitRepositoryConfig.getGitPassword())) {
                return isLogin(model, username, password, false);
            } else if (StringUtils.hasLength(gitRepositoryConfig.getGitPassword())
                    && StringUtils.hasLength(gitRepositoryConfig.getGitUsername())) {
                return "/list";
            } else {
                return "/login";
            }
        }
        if ((!StringUtils.hasLength(username) || !StringUtils.hasLength(password))
                && StringUtils.hasLength(gitRepositoryConfig.getGitPassword())
                && StringUtils.hasLength(gitRepositoryConfig.getGitUsername())) {
            return "/list";
        }
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return "/login";
        }
        String filePath = Objects.requireNonNull(YamlWriter.class.getResource("/application-git.yml")).getFile();
        String mainFilePath = Objects.requireNonNull(YamlWriter.class.getResource("/application.yml")).getFile();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                // 关键配置：禁用字符串自动引号
                .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                .enable(YAMLGenerator.Feature.INDENT_ARRAYS));

        GitRepositoryConfig appConfig = mapper.readValue(new File(filePath), GitRepositoryConfig.class);
        Map<String, Object> map = mapper.readValue(new File(mainFilePath), Map.class);
        List<String> gitProjects = appConfig.getProjects().stream()
                .map(GitProject::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(gitProjects)) {
            return "/login";
        }
        boolean isLogin = gitRepositoryService.isLogin(username, password, gitProjects.get(0));
        if (isLogin) {
            if (!username.equals(gitRepositoryConfig.getGitUsername())
                    || !password.equals(gitRepositoryConfig.getGitPassword())) {
                gitRepositoryConfig.setGitUsername(username);
                gitRepositoryConfig.setGitPassword(password);
            }
            map.put("git-username", username);
            map.put("git-password", password);
//            mapper.writeValue(new File(mainFilePath), map);
            logger.info("用户{}登录成功", username);
            return "/list";
        }
        return "/login";
    }

    /**
     * 列表展示
     *
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String list(Model model) {
        logger.info("进入列表展示");
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        if(CollectionUtils.isEmpty(projects)){
            return "list";
        }
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
//                e.printStackTrace();
                logger.error("更新git项目信息失败", e);
            } catch (GitAPIException e) {
//                e.printStackTrace();
                logger.error("更新git项目信息失败", e);
            }
        }
        modelBuild(model, projects);
        return "list";
    }

    @RequestMapping("/describeDepotMergeReqList")
    public String describeDepotMergeReqList(Model model) {
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        if(CollectionUtils.isEmpty(projects)){
            return "list";
        }
        Map<String, List<CodingMergersInfo>> codingMergersInfoMap = new HashMap<>();
        for(GitProject gitProject : projects){
            try {
                List<CodingMergersInfo> codingMergersInfos = gitRepositoryService.describeDepotMergeRequests(gitProject);
                List<String> infos = codingMergersInfos.stream().map(CodingMergersInfo::showInfo).collect(Collectors.toList());
                codingMergersInfoMap.put(gitProject.getName(), codingMergersInfos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //查询仓库合并请求列表
        model.addAttribute("codingMergersInfoMap", codingMergersInfoMap);
        return list(model);
    }

    @PostMapping("/setGitAccount")
    public String setGitAccount(Model model, String username, String password, String token) {
        gitRepositoryConfig.setGitUsername(username);
        gitRepositoryConfig.setGitPassword(password);
        if (StringUtils.hasLength(token)) {
            gitRepositoryConfig.setToken(token);
        }
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        modelBuild(model, projects);
        return "list";
    }

    /**
     * 增加仓库
     * @param model
     * @param gitProject
     * @return
     * @throws Exception
     */
    @PostMapping("/setGitProject")
    public String setGitProject(Model model, String gitProject) throws Exception {
        List<String> projectUrls = Arrays.asList(gitProject.split(";"));
        gitRepositoryConfig.setGitProjects(projectUrls, yamlWriter);
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        modelBuild(model, projects);
        return "list";
    }

    /**
     * 克隆或者拉取最新代码
     *
     * @param model
     * @return
     */
    @RequestMapping({"cloneOrPull/{pullBranch}/{project}"})
    public String cloneOrPull(Model model, @PathVariable(value = "pullBranch") String pullBranch,
                @PathVariable(value = "project") String project) {
        try {
            pullBranch = URLDecoder.decode(pullBranch, "UTF-8");
            project = URLDecoder.decode(project, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        if(!"all".equals(project)){
            List<String> mergeProjects = Arrays.stream(project.split(",")).map(String::trim).collect(Collectors.toList());
            projects = projects.stream().filter(p -> mergeProjects.contains(p.getName())).collect(Collectors.toList());
        }
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.switchBranch(gitProject, pullBranch);
                gitRepositoryService.cloneOrPull(gitProject);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        modelBuild(model, projects);
        buildPom(projects);
        return INDEX_HTML;
    }

    /**
     * 创建分支
     *
     * @param model
     * @return
     */
    @RequestMapping({"/createBranch/{branchName}"})
    public String createBranch(Model model, @PathVariable(value = "branchName") String branchName) {
        try {
            branchName = URLDecoder.decode(branchName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.createBranch(gitProject, branchName);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
            gitProject.getBranchList().add(branchName);
        }
        modelBuild(model, projects);
        return INDEX_HTML;
    }

    /**
     * 切换分支
     *
     * @param model
     * @return
     */
    @RequestMapping({"/switchBranch/{branchName}"})
    public String switchBranchAll(Model model, @PathVariable(value = "branchName") String branchName) {
        try {
            branchName = URLDecoder.decode(branchName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.switchBranch(gitProject, branchName);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        modelBuild(model, projects);
        return INDEX_HTML;
    }

    /**
     * 切换分支
     *
     * @param model
     * @return
     */
    @RequestMapping({"/switchBranchByProject/{branchName}/{project}"})
    public String switchBranchByProject(Model model,
                               @PathVariable(value = "branchName") String branchName,
                               @PathVariable(value = "project") String project) {
        try {
            branchName = URLDecoder.decode(branchName, "UTF-8");
            project = URLDecoder.decode(project, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        if(!"all".equals(project)){
            List<String> mergeProjects = Arrays.stream(project.split(",")).map(String::trim).collect(Collectors.toList());
            projects = projects.stream().filter(p -> mergeProjects.contains(p.getName())).collect(Collectors.toList());
        }
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.switchBranch(gitProject, branchName);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        modelBuild(model, projects);
        return INDEX_HTML;
    }

    /**
     * 推送代码
     *
     * @param model
     * @return
     */
    @RequestMapping({"/push/{message}"})
    public String push(Model model, @PathVariable(value = "message") String inputMessage) {
        try {
            inputMessage = URLDecoder.decode(inputMessage, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.push(gitProject, inputMessage);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        modelBuild(model, projects);
        return INDEX_HTML;
    }

    /**
     * 删除项目的当前分支，不允许删除master和develop
     *
     * @param model
     * @return
     */
    @RequestMapping({"/deleteBranch"})
    public String deleteBranch(Model model) {
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.deleteBranch(gitProject);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        modelBuild(model, projects);
        return INDEX_HTML;
    }

    /**
     * 创建标签
     *
     * @param model
     * @param tagName
     * @param tagLog
     * @return
     */
    @RequestMapping({"/createTag/{tagName}/{tagLog}"})
    public String createTag(Model model, @PathVariable(value = "tagName") String tagName, @PathVariable(value = "tagLog") String tagLog) {
        try {
            tagName = URLDecoder.decode(tagName, "UTF-8");
            tagLog = URLDecoder.decode(tagLog, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.createTag(gitProject, tagName, tagLog);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        modelBuild(model, projects);
        return INDEX_HTML;
    }

    /**
     * 从某标签检出代码到某新分支
     *
     * @param model
     * @param tagName
     * @param branchName
     * @return
     */
    @RequestMapping({"/createBranchByTag/{tagName}/{branchName}"})
    public String createBranchByTag(Model model, @PathVariable(value = "tagName") String tagName, @PathVariable(value = "branchName") String branchName) {
        try {
            tagName = URLDecoder.decode(tagName, "UTF-8");
            branchName = URLDecoder.decode(branchName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.createBranchByTag(gitProject, tagName, branchName);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        modelBuild(model, projects);
        return INDEX_HTML;
    }

    /**
     * 文件状态列表详情
     *
     * @param model
     * @param projectName
     * @return
     */
    @RequestMapping({"/fileListDetails/{name}"})
    public String fileListDetails(Model model, @PathVariable(value = "name") String projectName) {
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            if (projectName.equals(gitProject.getName())) {
                try {
                    gitRepositoryService.updateGitProjectInfo(gitProject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GitAPIException e) {
                    e.printStackTrace();
                }
                model.addAttribute("project", gitProject);
            }
        }
        return "fileListDetails";
    }

    /**
     * 新增文件预览
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping({UNTRACKED_FILE_VIEW_PATH_PREFIX + "{projectName}/**"})
    public String untrackedFileView(Model model, @PathVariable(value = "projectName") String projectName, HttpServletRequest request) {
        String path = request.getRequestURI().substring(UNTRACKED_FILE_VIEW_PATH_PREFIX.length(), request.getRequestURI().length());
        model.addAttribute("exception", false);
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        String modulesHome = gitRepositoryConfig.getModulesHome();
        for (GitProject gitProject : projects) {
            if (projectName.equals(gitProject.getName())) {
                try {
                    String fileName = URLDecoder.decode(path.substring(projectName.length() + 1, path.length()), "UTF-8");
                    String filePath = modulesHome + File.separator + gitProject.getName() + File.separator + fileName;
                    String fileContent = gitRepositoryService.getFileContent(gitProject, fileName);
                    if (fileName.contains(File.separator)) {
                        fileName = fileName.substring(fileName.lastIndexOf(File.separator), fileName.length());
                    }
                    model.addAttribute("filePath", filePath);
                    model.addAttribute("fileContent", fileContent);
                    model.addAttribute("fileName", fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("exception", true);
                    model.addAttribute("exceptionMessage", e.getMessage());
                }

            }
        }
        return "untrackedFileView";
    }

    /**
     * 删除标签
     *
     * @param model
     * @param tagName
     * @return
     */
    @RequestMapping({"/deleteTag/{tagName}"})
    public String deleteTag(Model model, @PathVariable(value = "tagName") String tagName) {
        try {
            tagName = URLDecoder.decode(tagName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.deleteTag(gitProject, tagName);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        modelBuild(model, projects);
        return INDEX_HTML;
    }

    /**
     * 合并分支，将被合并分支的修改并入当前工作分支
     *
     * @param model
     * @param currWorkBranch 当前工作分支
     * @param sourceBranch   被合并分支
     * @return
     */
    @RequestMapping({"/mergeBranch/{currWorkBranch}/{sourceBranch}/{message}"})
    public String mergeBranch(Model model, @PathVariable(value = "currWorkBranch") String currWorkBranch,
                              @PathVariable(value = "sourceBranch") String sourceBranch,
                              @PathVariable(value = "message") String message) {
        try {
            currWorkBranch = URLDecoder.decode(currWorkBranch, "UTF-8");
            sourceBranch = URLDecoder.decode(sourceBranch, "UTF-8");
            message = URLDecoder.decode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.mergeBranch(gitProject, currWorkBranch, sourceBranch, message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        modelBuild(model, projects);
        return INDEX_HTML;
    }


    /**
     * 创建合并请求，将被合并分支的修改并入当前工作分支
     *
     * @param model
     * @param currWorkBranch 当前工作分支
     * @param sourceBranch   被合并分支
     * @return
     */
    @RequestMapping({"/mergeBranchReq/{currWorkBranch}/{sourceBranch}/{message}/{content}/{project}"})
    public String mergeBranchReq(Model model, @PathVariable(value = "currWorkBranch") String currWorkBranch,
                              @PathVariable(value = "sourceBranch") String sourceBranch,
                              @PathVariable(value = "message") String message,
                              @PathVariable(value = "content") String content,
                              @PathVariable(value = "project") String project) {
        try {
            currWorkBranch = URLDecoder.decode(currWorkBranch, "UTF-8");
            sourceBranch = URLDecoder.decode(sourceBranch, "UTF-8");
            message = URLDecoder.decode(message, "UTF-8");
            content = URLDecoder.decode(content, "UTF-8");
            project = URLDecoder.decode(project, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        if(!"all".equals(project)){
            List<String> mergeProjects = Arrays.stream(project.split(",")).map(String::trim).collect(Collectors.toList());
            projects = projects.stream().filter(p -> mergeProjects.contains(p.getName())).collect(Collectors.toList());
        }
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.mergeBranchReq(gitProject, currWorkBranch, sourceBranch, message, content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        modelBuild(model, projects);
        return INDEX_HTML;
    }

    /**
     * 分页获取Git日志
     *
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping({"/getCommitLogs", "/getCommitLogs/{username}", "/getCommitLogs/{username}/{projectName}"})
    public List<GitLog> getCommitLogs(
            @PathVariable(value = "username", required = false) String username,
            @PathVariable(value = "projectName", required = false) String projectName) {
        Page<GitLog> page = new Page<>();
        page.setPageIndex(0);
        try {
            gitRepositoryService.getCommitLogs(page, username, projectName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        Set<String> userSet = new HashSet<>();
        for (GitLog log : page.getData()) {
            userSet.add(log.getUsername());
        }
        String[] userArr = new String[userSet.size()];
        userArr = userSet.toArray(userArr);
        Arrays.sort(userArr);
        return page.getAllData();
    }


    /**
     * 构建model属性
     *
     * @param model
     * @param projects
     * @param objects
     */
    private void modelBuild(Model model, List<GitProject> projects, Object... objects) {
        Map<String, GitProject> gitProjectMap = projects.stream().collect(Collectors.toMap(GitProject::getName, Function.identity()));
        /**
         * 添加项目集合属性
         */
        model.addAttribute("projects", projects);
        model.addAttribute("gitProjectMap", gitProjectMap);
        List<String> projectNames = projects.stream().map(GitProject::getName).collect(Collectors.toList());
        model.addAttribute("projectNames", projectNames);
        /**
         * 添加分支属性
         */
        List<String> branchIntersect = gitRepositoryService.getBranchIntersect(projects);
        model.addAttribute("branchIntersect", branchIntersect);
        /**
         * 添加标签属性
         */
        List<String> tagIntersect = gitRepositoryService.getTagIntersect(projects);
        model.addAttribute("tagIntersect", tagIntersect);
        model.addAttribute("gitRepositoryConfig", gitRepositoryConfig);
        model.addAllAttributes(Arrays.asList(objects));
    }

    private void modelMergeBuild(Model model, List<GitProject> projects, boolean isShowMergerList, Object... objects) {
        if (isShowMergerList) {
            Map<String, List<CodingMergersInfo>> codingMergersInfoMap = new HashMap<>();
            for(GitProject gitProject : projects){
                try {
                    List<CodingMergersInfo> codingMergersInfos = gitRepositoryService.describeDepotMergeRequests(gitProject);
                    List<String> infos = codingMergersInfos.stream().map(CodingMergersInfo::showInfo).collect(Collectors.toList());
                    codingMergersInfoMap.put(gitProject.getName(), codingMergersInfos);
                } catch (IOException e) {
                    logger.info("读取合并请求列表异常：" + e);
                    throw new RuntimeException(e);
                }
            }
            //查询仓库合并请求列表
            model.addAttribute("codingMergersInfoMap", codingMergersInfoMap);
        }
        /**
         * 添加项目集合属性
         */
        model.addAttribute("projects", projects);
        model.addAttribute("gitRepositoryConfig", gitRepositoryConfig);
        model.addAllAttributes(Arrays.asList(objects));
    }


    /**
     * 构建最上层的pom文件
     *
     * @param projects
     */
    private void buildPom(List<GitProject> projects) {
        File file = new File(gitRepositoryConfig.getModulesHome());
        try {
            Document document = DocumentHelper.parseText(IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("pom_template.xml"), Charset.forName("UTF-8")));
            Element modules = document.getRootElement().element("modules");
            Iterator<Element> it = modules.elementIterator();
            while (it.hasNext()) {
                Element element = it.next();
                modules.remove(element);
            }
            for (GitProject project : projects) {
                Element module = modules.addElement("module");
                module.setText("modules/" + project.getName());
            }
            String pomPath = file.getParent() + File.separator + "pom.xml";
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(pomPath), format);
            xmlWriter.write(document);
            xmlWriter.close();
            logger.info("更新本地最上层pom文件，pom文件路径：" + pomPath);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
