ALTER TABLE listings.listing_image ADD COLUMN image_order INTEGER;

WITH OrderedImages AS (
    SELECT
        id,
        ROW_NUMBER() OVER (PARTITION BY listing_id ORDER BY id) - 1 AS new_order
    FROM
        listings.listing_image
)
UPDATE listings.listing_image
    SET image_order = OrderedImages.new_order
    FROM OrderedImages
WHERE listings.listing_image.id = OrderedImages.id;