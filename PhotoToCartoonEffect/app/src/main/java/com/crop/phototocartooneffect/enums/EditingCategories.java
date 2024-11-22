package com.crop.phototocartooneffect.enums;

public class EditingCategories {
    public enum ImageCreationType {
        FIREBASE_ML_SEGMENTATION("Firebase_ML_Segmentation"),
        MLB_AI_AVATAR("MLB_Avatar"),
        IMAGE_EFFECT_IMG2IMG("MLB_Img2Img"), IMAGE_EFFECT_FASHION("MLB_fashion"), MLB_BACKGROUND_REMOVE("MLB_Background_Remove"),
        MONSTER_AI_IMG_TO_IMG("Monster_AI_Img_Img"), MONSTER_AI_PHOTO_MAKER("Monster_AI_Photo_Maker"), MONSTER_AI_PIX_TO_PIX("Monster_AI_Pix_Pix");

        private final String value;

        ImageCreationType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ImageCreationType fromString(String value) {
            for (ImageCreationType type : ImageCreationType.values()) {
                if (type.getValue().equalsIgnoreCase(value)) {
                    return type;
                }
            }
            return ImageCreationType.FIREBASE_ML_SEGMENTATION; // Default case if no match is found
        }
    }

    public enum AITypeFirebaseClothTypeEDB {
        NONE("none"), UPPER("upper_body"), BOTTOM("lower_body");

        private final String value;

        AITypeFirebaseClothTypeEDB(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static AITypeFirebaseClothTypeEDB fromString(String value) {
            for (AITypeFirebaseClothTypeEDB type : AITypeFirebaseClothTypeEDB.values()) {
                if (type.getValue().equalsIgnoreCase(value)) {
                    return type;
                }
            }
            return AITypeFirebaseClothTypeEDB.NONE; // Default case if no match is found
        }
    }

    public enum AITypeFirebaseEDB {
        FEATUREAI("featureai"), FEATUREAI2("featureai2"), DRESSES("fashion"), USERCREATIONS("user_creations"), USERCREATIONS_BANNER("user_creations_banner"), UNKNOWN("unknown");

        private final String value;

        AITypeFirebaseEDB(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static AITypeFirebaseEDB fromString(String value) {
            for (AITypeFirebaseEDB type : AITypeFirebaseEDB.values()) {
                if (type.getValue().equalsIgnoreCase(value)) {
                    return type;
                }
            }
            return AITypeFirebaseEDB.UNKNOWN; // Default case if no match is found
        }
    }
}