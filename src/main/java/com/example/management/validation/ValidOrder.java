package com.example.management.validation;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * バリデーションの順番を決定するインターフェース
 */
@GroupSequence({Default.class, ValidGroup1.class, ValidGroup2.class, ValidGroup3.class})
public interface ValidOrder {}
