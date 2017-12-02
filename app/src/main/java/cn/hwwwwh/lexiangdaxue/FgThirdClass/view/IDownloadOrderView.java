package cn.hwwwwh.lexiangdaxue.FgThirdClass.view;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.FgThirdClass.bean.Order;

/**
 * Created by 97481 on 2017/10/16/ 0016.
 */

public interface IDownloadOrderView {

    void initOrder(List<Order.OrderDataBean> list);

    void initOrderFail(String msg);
}
