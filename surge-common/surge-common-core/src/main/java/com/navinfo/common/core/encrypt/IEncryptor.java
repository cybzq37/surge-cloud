package com.surge.common.core.encrypt;

import com.surge.common.core.encrypt.enums.AlgorithmType;
import com.surge.common.core.encrypt.enums.EncodeType;

/**
 * 加解者
 *
 * @author 老马
 * @version 4.6.0
 */
public interface IEncryptor {

    /**
     * 获得当前算法
     */
    AlgorithmType algorithm();

    /**
     * 加密
     *
     * @param value      待加密字符串
     * @param encodeType 加密后的编码格式
     * @return 加密后的字符串
     */
    String encrypt(String value, EncodeType encodeType);

    /**
     * 解密
     *
     * @param value      待加密字符串
     * @return 解密后的字符串
     */
    String decrypt(String value);
}
