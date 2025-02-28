package com.tyron.completion;

import com.tyron.builder.project.Project;
import com.tyron.builder.project.api.Module;

import java.io.File;

/**
 * Contains useful information about the current completion request
 */
public class CompletionParameters {

    private final Project mProject;
    private final Module mModule;
    private final File mFile;
    private final String mContents;
    private final String mPrefix;
    private final int mLine;
    private final int mColumn;
    private final long mIndex;

    public static Builder builder() {
        return new Builder();
    }

    private CompletionParameters(Project project, Module module, File file, String contents,
                                 String prefix, int line, int column, long index) {
        mProject = project;
        mModule = module;
        mFile = file;
        mContents = contents;
        mPrefix = prefix;
        mLine = line;
        mColumn = column;
        mIndex = index;
    }

    public Project getProject() {
        return mProject;
    }

    public Module getModule() {
        return mModule;
    }

    public File getFile() {
        return mFile;
    }

    public String getContents() {
        return mContents;
    }

    public String getPrefix() {
        return mPrefix;
    }

    public int getLine() {
        return mLine;
    }

    public int getColumn() {
        return mColumn;
    }

    public long getIndex() {
        return mIndex;
    }

    public static final class Builder {

        private Project project;
        private Module module;
        private File file;
        private String contents;
        private String prefix;
        private int line;
        private int column;
        private long index;

        private Builder() {

        }

        public Builder setProject(Project project) {
            this.project = project;
            return this;
        }

        public Builder setModule(Module module) {
            this.module = module;
            return this;
        }

        public Builder setFile(File file) {
            this.file = file;
            return this;
        }

        public Builder setContents(String contents) {
            this.contents = contents;
            return this;
        }

        public Builder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder setLine(int line) {
            this.line = line;
            return this;
        }

        public Builder setColumn(int column) {
            this.column = column;
            return this;
        }

        public Builder setIndex(long index) {
            this.index = index;
            return this;
        }

        public CompletionParameters build() {
            return new CompletionParameters(project, module,
                    file, contents, prefix, line, column, index);
        }
    }
}
