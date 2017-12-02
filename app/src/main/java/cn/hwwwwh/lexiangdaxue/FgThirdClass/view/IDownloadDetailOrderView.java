package cn.hwwwwh.lexiangdaxue.FgThirdClass.view;

import cn.hwwwwh.lexiangdaxue.FgThirdClass.bean.DetailOrder;

/**
 * Created by 97481 on 2017/10/23/ 0023.
 */

public interface IDownloadDetailOrderView {

    void initOrder(DetailOrder.OrderDataBean detailOrder);

    void initOrderFail(String msg);
}
