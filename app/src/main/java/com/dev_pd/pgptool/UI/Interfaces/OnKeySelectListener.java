package com.dev_pd.pgptool.UI.Interfaces;

import com.dev_pd.pgptool.Cryptography.KeySerializable;

public interface OnKeySelectListener {
    void onKeySelect(KeySerializable keySerializable, String keyPath);
}
