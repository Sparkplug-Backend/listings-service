package com.sparkplug.listings.application.mapper;

import com.sparkplug.listings.application.dto.CarModificationDto;
import com.sparkplug.listings.application.dto.ListingDto;
import com.sparkplug.listings.domain.model.Listing;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ListingMapper {

    @Mapping(target = "id", source = "listing.id")
    @Mapping(target = "images", source = "listing.images")
    @Mapping(target = "price", source = "listing.price.amount")
    @Mapping(target = "mileage", source = "listing.mileage.value")
    ListingDto toDto(Listing listing, CarModificationDto carModification);
}