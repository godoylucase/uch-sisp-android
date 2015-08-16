package com.uch.sisp.client.gcm.http.request.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lucas on 15/08/15.
 */
@Getter @Setter @Builder
public class GPSPosition implements Serializable {
    private BigDecimal latitude;
    private BigDecimal longitude;
}
