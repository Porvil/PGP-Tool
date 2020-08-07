package com.dev_pd.pgptool.UI;

import com.dev_pd.pgptool.Cryptography.KeySerializable;

public interface OnKeySelectListener {
    void onKeySelect(KeySerializable keySerializable, String keyPath);
}
