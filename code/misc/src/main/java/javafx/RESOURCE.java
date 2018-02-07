package javafx;

public enum RESOURCE {
  GREETING("greeting"), PASSWORD_PROMPT("passwordPrompt"), PASSWORD_LABEL("passwordLabel");

  private String key;

  RESOURCE(String key) {
    this.key = key;
  }

  public String toString() {
    return key;
  }
}
