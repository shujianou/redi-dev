package com.wwdj.admin.controller.upload;

import com.wwdj.manager.file.dto.FileUploadRequest;
import com.wwdj.manager.file.entity.AttachmentEntity;
import com.wwdj.manager.file.service.AttachmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.redimybase.framework.bean.R;
import com.redimybase.security.utils.SecurityUtil;
import com.wwdj.manager.security.entity.AppUserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

/**
 * 文件控制器
 * Created by Vim 2018/12/26 16:22
 *
 * @author Vim
 */
@RestController
@RequestMapping("file")
@Slf4j
@Api(tags = "文件操作相关接口")
public class FileController {


    @PostMapping(value = "upload")
    @ApiOperation(value = "文件上传")
    public R<?> upload(@ApiParam(value = "需要上传的附件") @RequestParam("file") MultipartFile file, FileUploadRequest fileUploadRequest) {
        if (file.isEmpty()) {
            return R.fail("请选择需要上传的文件");
        }
        String type = fileUploadRequest.getType();
        String folder = fileUploadRequest.getFolder();

        if (StringUtils.isBlank(type)) {
            return R.fail("文件类型不能为空");
        }

        if (StringUtils.isBlank(folder)) {
            return R.fail("上传的文件夹不能为空");
        }
        //文件名
        String fileName = file.getOriginalFilename();

        if (StringUtils.isBlank(fileName)) {
            return R.fail("文件名为空,请重新选择文件");
        }

        //文件后缀类型
        String suffixType = fileName.substring(fileName.lastIndexOf(".") + 1);

        String folderPath = fileSavePath + folder + "/";

        try {
            AppUserEntity userEntity = SecurityUtil.getCurrentUser();
            if (null == userEntity) {
                return R.fail("用户凭证过期,请尝试重新登录");
            }


            AttachmentEntity attachmentEntity = new AttachmentEntity();
            attachmentEntity.setId(IdWorker.getIdStr());
            String filePath = folderPath + attachmentEntity.getId() + "." + suffixType;

            attachmentEntity.setCreateTime(new Date());
            attachmentEntity.setCreator(userEntity.getUserName());
            attachmentEntity.setCreatorId(userEntity.getId());
            attachmentEntity.setName(fileName);
            attachmentEntity.setType(type);
            attachmentEntity.setSize(String.valueOf(file.getSize()));
            attachmentEntity.setSuffixType(suffixType);
            attachmentEntity.setPath(filePath);

            attachmentService.save(attachmentEntity);


            File dest = new File(filePath);

            if (!dest.getParentFile().exists()) {
                //判断文件父目录是否存在
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);


            return R.ok(attachmentEntity);
        } catch (IOException e) {
            log.error(e.toString(), e);
            return R.fail("文件上传失败:" + e.getMessage());
        }
    }


    @PostMapping(value = "multiUpload")
    @ApiOperation(value = "上传多个附件")
    public R<?> multiUpload(@ApiParam(value = "需要上传的多个附件") @RequestParam("file") MultipartFile[] files, @ApiParam(value = "附件类型") String type, @ApiParam(value = "上传到哪个文件夹") String folder) {
        AppUserEntity userEntity = SecurityUtil.getCurrentUser();
        if (null == userEntity) {
            return R.fail("用户凭证过期,请尝试重新登录");
        }


        if (files.length == 0) {
            return R.fail("请选择需要上传的文件");
        }

        String folderPath = fileSavePath + folder + "/";

        try {

            //附件ID列表
            List<String> idList = new ArrayList<>();

            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();

                if (file.isEmpty()) {
                    return R.fail("请选择需要上传的文件");
                } else {
                    if (StringUtils.isBlank(fileName)) {
                        continue;
                    }
                    //文件后缀类型
                    String suffixType = fileName.substring(fileName.lastIndexOf(".") + 1);

                    AttachmentEntity attachmentEntity = new AttachmentEntity();
                    attachmentEntity.setId(IdWorker.getIdStr());

                    String filePath = folderPath + attachmentEntity.getId() + "." + suffixType;

                    attachmentEntity.setSize(String.valueOf(file.getSize()));
                    attachmentEntity.setCreateTime(new Date());
                    attachmentEntity.setCreator(userEntity.getUserName());
                    attachmentEntity.setCreatorId(userEntity.getId());
                    attachmentEntity.setName(fileName);
                    attachmentEntity.setType(type);
                    attachmentEntity.setSuffixType(suffixType);
                    attachmentEntity.setPath(filePath);

                    attachmentService.save(attachmentEntity);

                    File dest = new File(filePath);
                    if (!dest.getParentFile().exists()) {
                        //判断文件父目录是否存在
                        dest.getParentFile().mkdirs();
                    }
                    file.transferTo(dest);

                    idList.add(attachmentEntity.getId());
                }
            }

            return R.ok(idList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }


    @RequestMapping(value = "download", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "文件下载")
    public String downLoad(HttpServletResponse response, @ApiParam(value = "需要下载的附件ID") String attachmentId) {

        AttachmentEntity attachmentEntity = attachmentService.getOne(new QueryWrapper<AttachmentEntity>().eq("id", attachmentId).select("path"));

        String filePath = attachmentEntity.getPath();
        File file = new File(attachmentEntity.getPath());
        if (file.exists()) {
            //判断文件父目录是否存在
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + filePath.substring(filePath.lastIndexOf("\\") + 1));

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;

            OutputStream os;
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    @Value("${redi.file.save.path}")
    private String fileSavePath;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AttachmentService attachmentService;
}
