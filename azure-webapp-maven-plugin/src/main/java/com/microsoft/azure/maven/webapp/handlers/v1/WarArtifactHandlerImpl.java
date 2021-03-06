/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.maven.webapp.handlers.v1;

import com.google.common.io.Files;
import com.microsoft.azure.maven.artifacthandler.ArtifactHandler;
import com.microsoft.azure.maven.deploytarget.DeployTarget;
import com.microsoft.azure.maven.webapp.AbstractWebAppMojo;
import com.microsoft.azure.maven.webapp.handlers.ArtifactHandlerUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.nio.file.Paths;

/**
 * Artifact handler used to deploy war file. Typically, each artifact handler needs to extends
 * {@link com.microsoft.azure.maven.artifacthandler.ArtifactHandlerBase}
 * WarArtifactHandler is a special case because it does not need staging folder.
 * Thus, the methods shared in ArtifactHandlerBase is not needed here.
 */
public class WarArtifactHandlerImpl implements ArtifactHandler {

    public static final String FILE_IS_NOT_WAR = "The deployment file is not a war typed file.";
    public static final String FIND_WAR_FILE_FAIL = "Failed to find the war file: '%s'";
    public static final String UPLOAD_FAILURE = "Exception occurred when deploying war file to server: %s, " +
        "retrying immediately (%d/%d)";
    public static final String DEPLOY_FAILURE = "Failed to deploy war file after %d times of retry.";
    public static final int DEFAULT_MAX_RETRY_TIMES = 3;

    private AbstractWebAppMojo mojo;

    public WarArtifactHandlerImpl(final AbstractWebAppMojo mojo) {
        this.mojo = mojo;
    }

    @Override
    public void publish(final DeployTarget target) throws MojoExecutionException {

        final File war = getWarFile();

        assureWarFileExisted(war);

        final Runnable warDeployExecutor = ArtifactHandlerUtils.getRealWarDeployExecutor(target, war, getContextPath());
        
        // Add retry logic here to avoid Kudu's socket timeout issue.
        // More details: https://github.com/Microsoft/azure-maven-plugins/issues/339
        int retryCount = 0;
        mojo.getLog().info("Deploying the war file...");

        while (retryCount < DEFAULT_MAX_RETRY_TIMES) {
            retryCount++;
            try {
                warDeployExecutor.run();
                return;
            } catch (Exception e) {
                mojo.getLog().debug(String.format(UPLOAD_FAILURE, e.getMessage(), retryCount, DEFAULT_MAX_RETRY_TIMES));
            }
        }

        throw new MojoExecutionException(String.format(DEPLOY_FAILURE, retryCount));
    }

    protected String getContextPath() {
        String path = mojo.getPath().trim();
        if (path.charAt(0) == '/') {
            path = path.substring(1);
        }
        return path;
    }

    protected File getWarFile() {
        return StringUtils.isNotEmpty(mojo.getWarFile()) ? new File(mojo.getWarFile()) :
                    new File(Paths.get(mojo.getBuildDirectoryAbsolutePath(),
                            mojo.getProject().getBuild().getFinalName() + ".war").toString());
    }

    protected void assureWarFileExisted(final File war) throws MojoExecutionException {
        if (!Files.getFileExtension(war.getName()).equalsIgnoreCase("war")) {
            throw new MojoExecutionException(FILE_IS_NOT_WAR);
        }

        if (!war.exists() || !war.isFile()) {
            throw new MojoExecutionException(String.format(FIND_WAR_FILE_FAIL, war.getAbsolutePath()));
        }
    }
}
