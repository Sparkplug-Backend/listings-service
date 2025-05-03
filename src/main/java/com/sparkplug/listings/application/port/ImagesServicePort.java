package com.sparkplug.listings.application.port;

import java.util.Map;

public interface ImagesServicePort {
    void reorderImages(Long listingId, Map<Long, Integer> idsAndOrders);
}
