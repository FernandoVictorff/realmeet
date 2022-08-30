package br.com.sw2you.realmeet.email.model;

import java.io.InputStream;
import java.util.Objects;

public class Attachment {
    private final InputStream inputStream;
    private final String contentType;
    private final String fileNeme;

    public Attachment(Builder builder) {
        inputStream = builder.inputStream;
        contentType = builder.contentType;
        fileNeme = builder.fileNeme;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileNeme() {
        return fileNeme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return (
            Objects.equals(inputStream, that.inputStream) &&
            Objects.equals(contentType, that.contentType) &&
            Objects.equals(fileNeme, that.fileNeme)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputStream, contentType, fileNeme);
    }

    @Override
    public String toString() {
        return (
            "Attachment{" +
            "inputStream=" +
            inputStream +
            ", contentType='" +
            contentType +
            '\'' +
            ", fileNeme='" +
            fileNeme +
            '\'' +
            '}'
        );
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private InputStream inputStream;
        private String contentType;
        private String fileNeme;

        private Builder() {}

        public Builder inputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder fileNeme(String fileNeme) {
            this.fileNeme = fileNeme;
            return this;
        }

        public Attachment build() {
            return new Attachment(this);
        }
    }
}
