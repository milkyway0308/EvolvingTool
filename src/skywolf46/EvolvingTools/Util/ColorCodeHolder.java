package skywolf46.EvolvingTools.Util;

public class ColorCodeHolder {
    public static final String CODE = convertToColor("Holder");
    // f = Color code white
    public static final String SUFFIX = convertToColor("Uf-Suffixf");
    public static String convertToColor(String n){
        char[] text = new char[n.length() * 2];
        for(int i = 0;i < n.length();i++){
            int x = i * 2;
            text[x] = '§';
            text[x+1] = n.charAt(i);
        }
        return new String(text);
    }

    public static String parseFromColor(String n){
        char[] text = new char[n.length()/2];
        for(int i = 1;i < n.length();i+=2){
            text[i/2] = n.charAt(i);
        }
        return new String(text);
    }

    public static void main(String[] args) {
        System.out.println(convertToColor("test"));
        System.out.println(parseFromColor(convertToColor("Hello, World!")));
    }
}
