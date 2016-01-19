package com.test.rmmfl.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data(staticConstructor = "of")
@AllArgsConstructor
@NoArgsConstructor
public class Tuple<T1, T2> {
    T1 t1;
    T2 t2;
}
