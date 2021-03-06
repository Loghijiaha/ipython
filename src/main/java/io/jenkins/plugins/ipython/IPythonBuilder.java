package io.jenkins.plugins.ipython;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import io.jenkins.plugins.ipython.interpreter.IPythonKernalInterepreter;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.IOException;

public class IPythonBuilder extends Builder implements SimpleBuildStep {

    private final String line;

    @DataBoundConstructor
    public IPythonBuilder(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        try {
            // Interpreter wil be called or created if null
            IPythonKernalInterepreter interpreter = IPythonKernalInterepreter.getInstance();
            // Console output
            listener.getLogger().println("Output : "+ interpreter.sendAndInterpret(line).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Symbol("python")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public FormValidation doCheckLine(@QueryParameter String value) {
            if (value.length() == 0)
                return FormValidation.error("Enter the script");
            return FormValidation.ok();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Python console ";
        }

    }



}
