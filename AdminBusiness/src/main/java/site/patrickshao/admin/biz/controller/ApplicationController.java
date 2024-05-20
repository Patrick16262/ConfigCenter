package site.patrickshao.admin.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import site.patrickshao.admin.biz.annotation.PreAuthorize;
import site.patrickshao.admin.biz.service.ApplicationService;
import site.patrickshao.admin.common.entity.po.AccessTokenPO;
import site.patrickshao.admin.common.entity.po.ApplicationPO;

import java.util.List;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/18
 */
@Controller
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @PreAuthorize("Application#Visible")
    public List<ApplicationPO> getVisible() {
        return applicationService.getVisibleApplicationList();
    }

    @PreAuthorize("Application#Create")
    public Long create(String applicationName, String nickName, String comment) {
        ApplicationPO applicationPO = new ApplicationPO();
        applicationPO.setApplicationName(applicationName);
        applicationPO.setComment(comment);
        applicationPO.setNickName(nickName);
        return applicationService.createApplication(applicationPO);
    }

    @PreAuthorize("Application#Delete")
    public void delete(Long applicationId) {
        applicationService.DelayedDeleteApplication(applicationId);
    }

    @PreAuthorize("Application#EnableRequireAcessKey")
    public void enableRequireAccessKey(Long applicationId) {
        applicationService.enableRequireAccessKey(applicationId);
    }

    @PreAuthorize("Application#DisableRequireAcessKey")
    public void disableRequireAccessKey(Long applicationId) {
        applicationService.disableRequireAccessKey(applicationId);
    }

    @PreAuthorize("Application#GenerateAceessKey")
    public void generateAccessKey(Long applicationId) {
        applicationService.generateAccessKey(applicationId);
    }

    @PreAuthorize("Application#EnableAccessKey")
    public void enableAccessKey(Long accessKeyId) {
        applicationService.enableAccessKey(accessKeyId);
    }

    @PreAuthorize("Application#DisableAccessKey")
    public void disableAccessKey(Long accessKeyId) {
        applicationService.disableAccessKey(accessKeyId);
    }

    @PreAuthorize("Application#ViewAccessKey")
    public List<AccessTokenPO> getAccessKeyList(Long applicationId) {
        return applicationService.getAccessKeyList(applicationId);
    }

    @PreAuthorize("Application#EditApplicationName")
    public void editApplicationName(Long applicationId, String newName) {
        applicationService.editApplicationName(applicationId, newName);
    }

    @PreAuthorize("Application#EditNickName")
    public void editNickName(Long applicationId, String newName) {
        applicationService.editApplicationNickname(applicationId, newName);
    }

    @PreAuthorize("Application#EditComment")
    public void editComment(Long applicationId, String newComment) {
        applicationService.editApplicationComment(applicationId, newComment);
    }
}