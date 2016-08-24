package com.ptb.uranus.common.entity;

public enum CollectType {

    C_WX_M_D {
        public int getCode() {
            return 0x10000001;
        }
    },   //微信媒体动态
    C_WX_M_S {
        public int getCode() {
            return 0x10000002;
        }
    },   //微信媒体静态
    C_WX_A_D {
        public int getCode() {
            return 0x10000003;
        }
    },   //微信文章动态
    C_WX_A_S {
        public int getCode() {
            return 0x10000004;
        }
    },   //微信文章静态
    C_WB_A_S {
        public int getCode() {
            return 0x10000005;
        }
    },   //微博文章静态
    C_WB_A_D {
        public int getCode() {
            return 0x10000006;
        }
    },   //微博文章动态
    C_WB_M_D {
        public int getCode() {
            return 0x10000007;
        }
    },   //微博媒体动态
    C_WB_M_S {
        public int getCode() {
            return 0x10000008;
        }
    },   //微博媒体静态
    C_A_A_D {
        public int getCode() {
            return 0x10000009;
        }
    },    //通用文章动态
    C_A_A_S {
        public int getCode() {
            return 0x1000000A;
        }
    },
    C_A_A_N {
        public int getCode() {
            return 0x1000000B;
        }
    },
    C_WB_A_N {
        public int getCode() {
            return 0x1000000C;
        }
    },
    C_WX_A_N {
        public int getCode() {
            return 0x1000000D;
        }
    };

    public int getCode() {
        return 0x10000000;
    }
}