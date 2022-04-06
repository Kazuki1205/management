package com.example.management.validation;

import javax.validation.GroupSequence;

/**
 * バリデーションの順番を決定するインターフェース
 */
@GroupSequence({ValidGroup1.class, ValidGroup2.class})
public interface ValidOrder {

}
