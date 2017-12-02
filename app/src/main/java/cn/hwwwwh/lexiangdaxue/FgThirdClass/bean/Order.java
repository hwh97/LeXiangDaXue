package cn.hwwwwh.lexiangdaxue.FgThirdClass.bean;

import java.util.List;

/**
 * Created by 97481 on 2017/10/16/ 0016.
 */

public class Order {

    /**
     * error : false
     * orderData : [{"name":"黄先生","order_totalprice":4.4,"order_type":1,"order_status":1,"order_totalGoodsNum":6,"order_sn":"2017101650494910","goods":[{"og_id":22,"order_id":14,"og_goods_name":"双汇 香辣香脆肠 35g/包","og_goods_price":1.2,"og_goods_num":2,"og_goods_totalprice":2.4,"og_goods_image":"https://i.loli.net/2017/09/06/59af66c728406.jpg"},{"og_id":23,"order_id":14,"og_goods_name":"咪咪虾条 马来风味 20g/包","og_goods_price":0.5,"og_goods_num":4,"og_goods_totalprice":2,"og_goods_image":"https://i.loli.net/2017/09/06/59af6bffad171.jpg"}]},{"name":"黄先生","order_totalprice":90.5,"order_type":2,"order_status":1,"order_totalGoodsNum":4,"order_sn":"2017101656489950","goods":[{"og_id":24,"order_id":15,"og_goods_name":"草莓/盒","og_goods_price":10.5,"og_goods_num":1,"og_goods_totalprice":10.5,"og_goods_image":"https://i.loli.net/2017/09/06/59af8c1338561.jpg"},{"og_id":25,"order_id":15,"og_goods_name":"蜜橘/个","og_goods_price":2,"og_goods_num":1,"og_goods_totalprice":2,"og_goods_image":"https://i.loli.net/2017/09/06/59af8c885061b.jpg"},{"og_id":26,"order_id":15,"og_goods_name":"南非西柚/个","og_goods_price":75,"og_goods_num":1,"og_goods_totalprice":75,"og_goods_image":"https://i.loli.net/2017/09/06/59af8cc4a387b.jpg"},{"og_id":27,"order_id":15,"og_goods_name":"苹果(红富士)/个","og_goods_price":3,"og_goods_num":1,"og_goods_totalprice":3,"og_goods_image":"https://i.loli.net/2017/09/06/59af8cdc33882.jpg"}]}]
     */

    private boolean error;
    private List<OrderDataBean> orderData;
    private String error_msg;

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<OrderDataBean> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<OrderDataBean> orderData) {
        this.orderData = orderData;
    }

    public static class OrderDataBean {
        /**
         * name : 黄先生
         * order_totalprice : 4.4
         * order_type : 1
         * order_status : 1
         * order_totalGoodsNum : 6
         * order_sn : 2017101650494910
         * goods : [{"og_id":22,"order_id":14,"og_goods_name":"双汇 香辣香脆肠 35g/包","og_goods_price":1.2,"og_goods_num":2,"og_goods_totalprice":2.4,"og_goods_image":"https://i.loli.net/2017/09/06/59af66c728406.jpg"},{"og_id":23,"order_id":14,"og_goods_name":"咪咪虾条 马来风味 20g/包","og_goods_price":0.5,"og_goods_num":4,"og_goods_totalprice":2,"og_goods_image":"https://i.loli.net/2017/09/06/59af6bffad171.jpg"}]
         */

        private String name;
        private double order_totalprice;
        private int order_type;
        private int order_status;
        private int order_totalGoodsNum;
        private String order_sn;
        private List<GoodsBean> goods;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getOrder_totalprice() {
            return order_totalprice;
        }

        public void setOrder_totalprice(double order_totalprice) {
            this.order_totalprice = order_totalprice;
        }

        public int getOrder_type() {
            return order_type;
        }

        public void setOrder_type(int order_type) {
            this.order_type = order_type;
        }

        public int getOrder_status() {
            return order_status;
        }

        public void setOrder_status(int order_status) {
            this.order_status = order_status;
        }

        public int getOrder_totalGoodsNum() {
            return order_totalGoodsNum;
        }

        public void setOrder_totalGoodsNum(int order_totalGoodsNum) {
            this.order_totalGoodsNum = order_totalGoodsNum;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * og_id : 22
             * order_id : 14
             * og_goods_name : 双汇 香辣香脆肠 35g/包
             * og_goods_price : 1.2
             * og_goods_num : 2
             * og_goods_totalprice : 2.4
             * og_goods_image : https://i.loli.net/2017/09/06/59af66c728406.jpg
             */

            private int og_id;
            private int order_id;
            private String og_goods_name;
            private double og_goods_price;
            private int og_goods_num;
            private double og_goods_totalprice;
            private String og_goods_image;

            public int getOg_id() {
                return og_id;
            }

            public void setOg_id(int og_id) {
                this.og_id = og_id;
            }

            public int getOrder_id() {
                return order_id;
            }

            public void setOrder_id(int order_id) {
                this.order_id = order_id;
            }

            public String getOg_goods_name() {
                return og_goods_name;
            }

            public void setOg_goods_name(String og_goods_name) {
                this.og_goods_name = og_goods_name;
            }

            public double getOg_goods_price() {
                return og_goods_price;
            }

            public void setOg_goods_price(double og_goods_price) {
                this.og_goods_price = og_goods_price;
            }

            public int getOg_goods_num() {
                return og_goods_num;
            }

            public void setOg_goods_num(int og_goods_num) {
                this.og_goods_num = og_goods_num;
            }

            public double getOg_goods_totalprice() {
                return og_goods_totalprice;
            }

            public void setOg_goods_totalprice(double og_goods_totalprice) {
                this.og_goods_totalprice = og_goods_totalprice;
            }

            public String getOg_goods_image() {
                return og_goods_image;
            }

            public void setOg_goods_image(String og_goods_image) {
                this.og_goods_image = og_goods_image;
            }
        }
    }
}
