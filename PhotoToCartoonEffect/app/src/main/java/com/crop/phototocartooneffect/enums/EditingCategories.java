package com.crop.phototocartooneffect.enums;

public class EditingCategories {
    public enum ImageCreationType {
        FIREBASE_ML_SEGMENTATION("Firebase_ML_Segmentation"),
        MLB_AI_AVATAR("MLB_Avatar"),
        IMAGE_EFFECT_IMG2IMG("MLB_Img2Img"), IMAGE_EFFECT_FASHION("MLB_fashion"), MLB_BACKGROUND_REMOVE("MLB_Background_Remove"),
        MONSTER_AI_IMG_TO_IMG("Monster_AI_Img_Img"), MONSTER_AI_PHOTO_MAKER("Monster_AI_Photo_Maker"),
        MONSTER_AI_PIX_TO_PIX("Monster_AI_Pix_Pix"), AI_LAB_FACE_EXPRESSION("face_expression");

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
        NONE("dresses"), UPPER("upper_body"), BOTTOM("lower_body");

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

        public String getTitle() {
            String title = "Unknown";
            switch (fromString(value)) {
                case FEATUREAI:
                    title = "Feature AI";
                    break;
                case FEATUREAI2:
                    title = "Feature AI 2";
                    break;
                case USERCREATIONS:
                    title = "User Creations";
                    break;
                default:
                    title = "Unknown";
                    break;
            }
            return title.toUpperCase();
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

    public enum AILabExpressionType {
        NONE(-1),
        BIG_LAUGH(0),
        POUTING(1),
        FEEL_SAD(2),
        SMILE(3),
        DIMPLE_SMILE(10),
        PEAR_DIMPLE_SMILE(11),
        BIG_GRIN(12),
        STANDARD_GRIN(13),
        COOL_POSE(14),
        SAD(15),
        FORCED_SMILE(16),
        OPENING_EYES(100);

        private final int value;

        AILabExpressionType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public String getTitle() {
            String title;
            switch (fromString(value)) {
                case POUTING:
                    title = "Pouting";
                    break;
                case FEEL_SAD:
                    title = "Feel Sad";
                    break;
                case SMILE:
                    title = "Smile";
                    break;
                case DIMPLE_SMILE:
                    title = "Dimple Smile";
                    break;
                case PEAR_DIMPLE_SMILE:
                    title = "Pear Dimple Smile";
                    break;
                case BIG_GRIN:
                    title = "Big Grin";
                    break;
                case STANDARD_GRIN:
                    title = "Standard Grin";
                    break;
                case COOL_POSE:
                    title = "Cool Pose";
                    break;
                case SAD:
                    title = "Sad";
                    break;
                case FORCED_SMILE:
                    title = "Forced Smile";
                    break;
                case OPENING_EYES:
                    title = "Opening Eyes";
                    break;
                default:
                    title = "Big Laugh";
                    break;

            }
            return title.toUpperCase();
        }

        public static AILabExpressionType fromString2(String value) {
            return fromString(Integer.parseInt(value));
        }

        public static AILabExpressionType fromString(int value) {
            for (AILabExpressionType type : AILabExpressionType.values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            return AILabExpressionType.NONE; // Default case if no match is found
        }
    }
}