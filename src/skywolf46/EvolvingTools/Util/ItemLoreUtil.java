package skywolf46.EvolvingTools.Util;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class ItemLoreUtil {

    public static String tagToText(String... tags) {
        StringBuilder tagBuilder = new StringBuilder();
        for (int i = 0; i < tags.length; i++) {
            if (i != 0)
                tagBuilder.append("||");
            tagBuilder.append(tags[i]);
        }
        return ColorCodeHolder.convertToColor(tagBuilder.toString());
    }


    public static class TagData {
        private LinkedHashMap<String, String[]> tagData = new LinkedHashMap<>();

        public TagData(String tag) {
            if(tag.isEmpty())
                return;
            apply(tag);
        }


        public TagData() {

        }

        private void apply(String tag){
            tag = tag.substring(0,tag.length()-2);
            tag = ColorCodeHolder.parseFromColor(tag);
            String[] resplit = tag.split("\\|\\|");
            for (String n : resplit) {
                String[] reresplit = n.split("│");
                tagData.put(reresplit[0], Arrays.copyOfRange(reresplit, 1, reresplit.length));
            }
        }

        public String[] getTag(String tagName) {
            return tagData.get(tagName);
        }

        public TagData setTag(String tagName) {
            tagData.put(tagName, new String[0]);
            return this;
        }

        public TagData setTag(String tagName, String... tagData) {
            this.tagData.put(tagName, tagData);
            return this;
        }

        public TagData removeTag(String tagName) {
            tagData.remove(tagName);
            return this;
        }


        public String format() {
            if (tagData.size() <= 0)
                return "";
            return ItemLoreUtil.tagToText(tagToText()) + "§f";
        }

        public String[] tagToText() {
            String[] tags = new String[tagData.size()];
            List<String> lore = new ArrayList<>(tagData.keySet());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lore.size(); i++) {
                String[] txt = tagData.get(lore.get(i));
                sb.append(lore.get(i));
                for (String n : txt)
                    sb.append("│").append(n);
                tags[i] = sb.toString();
                sb.setLength(0);
            }
            return tags;
        }

        public List<String> getTagList() {
            return new ArrayList<>(tagData.keySet());
        }
    }
}
