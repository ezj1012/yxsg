package com.yxbear.sg.domain.bean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.yxbear.core.exception.CoreException;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@ConfigurationProperties(prefix = "sg")
public class SGProps {

    private Svr server = new Svr();

    /**
     * 免登录url,例如html,js,css等
     */
    private List<String> permitAll = new ArrayList<>();

    /**
     * 免权限
     */
    private List<String> permitPermissionAll = new ArrayList<>();

    private volatile File rsmDir = null;

    private String rsmPath = "./rsm";

    @Getter
    @Setter
    public static class Svr {

        private boolean autoInit = false;

        private String name;

        private Long openTime;

    }

    public File getRsmDir() {
        if (rsmDir == null) {
            synchronized (SGProps.class) {
                if (rsmDir == null) {
                    try {
                        rsmDir = new File(rsmPath).getCanonicalFile();
                        if (!rsmDir.exists()) {
                            rsmDir.mkdirs();
                        }
                    } catch (IOException e) {
                        throw new CoreException("rsm dir err " + rsmPath);
                    }
                    if (!rsmDir.exists() || rsmDir.isFile()) { throw new CoreException("rsm dir err " + rsmPath); }
                }
            }
        }
        return rsmDir;
    }

}
