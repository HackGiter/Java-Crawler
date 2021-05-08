public class urlLogs {
    private int number, size;
    private String url, file, errors;
    private boolean warning;

    public urlLogs(String string) {
        url = string;
    }

    public void set(int a, int b, String string) {
        number = a;
        size = b;
        file = string;
        warning = false;
    }

    public void somethingWrong (boolean warn, String error) {
        warning = warn;
        errors = error;
    }

    public String getString() {
        return url;
    }

    public String getFile() {
        return file;
    }

    public int read() {
        return size;
    }

    public boolean judge() { return warning; }

    public String getErrors() { return errors; }

}
