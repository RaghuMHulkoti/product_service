-- =====================================================================
-- PRODUCTS DB (TDD §9.2) — NO QUANTITY, NO PRICE: those live in
-- Inventory Service (quantity) and Supplier Service offers (price).
-- =====================================================================

CREATE TABLE categories (
    id          BIGSERIAL PRIMARY KEY,
    parent_id   BIGINT REFERENCES categories(id),
    slug        VARCHAR(120) NOT NULL UNIQUE,
    name        VARCHAR(160) NOT NULL,
    -- Slug chain from the root, e.g. '/electronics/mobiles/smartphones'.
    -- TDD suggests LTREE; VARCHAR + prefix index gives the same subtree query without the extension.
    path        VARCHAR(1000) NOT NULL,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    version     BIGINT  NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_categories_path ON categories (path varchar_pattern_ops);
CREATE INDEX idx_categories_parent ON categories (parent_id);

CREATE TABLE brands (
    id          BIGSERIAL PRIMARY KEY,
    slug        VARCHAR(120) NOT NULL UNIQUE,
    name        VARCHAR(160) NOT NULL,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE products (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sku             VARCHAR(64) NOT NULL,
    gtin            VARCHAR(20),
    title           VARCHAR(500) NOT NULL,
    description     TEXT,
    brand_id        BIGINT REFERENCES brands(id),
    category_id     BIGINT NOT NULL REFERENCES categories(id),
    attributes      JSONB NOT NULL DEFAULT '{}'::JSONB,
    media_root      VARCHAR(500),
    status          VARCHAR(20) NOT NULL DEFAULT 'DRAFT'
                    CHECK (status IN ('DRAFT','ACTIVE','HIDDEN','DELISTED')),
    weight_grams    INT,
    dim_l_mm        INT,
    dim_w_mm        INT,
    dim_h_mm        INT,
    is_hazardous    BOOLEAN NOT NULL DEFAULT FALSE,
    version         BIGINT NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at      TIMESTAMPTZ
);
-- SKU and GTIN are unique among live products; a soft-deleted product frees them for reuse.
CREATE UNIQUE INDEX uq_products_sku ON products (sku) WHERE deleted_at IS NULL;
CREATE UNIQUE INDEX uq_products_gtin ON products (gtin) WHERE gtin IS NOT NULL AND deleted_at IS NULL;
CREATE INDEX idx_products_category ON products (category_id) WHERE status = 'ACTIVE';
CREATE INDEX idx_products_brand    ON products (brand_id)    WHERE status = 'ACTIVE';
CREATE INDEX idx_products_attrs_gin ON products USING GIN (attributes);

CREATE TABLE product_media (
    id          BIGSERIAL PRIMARY KEY,
    product_id  UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    kind        VARCHAR(20) NOT NULL CHECK (kind IN ('IMAGE','VIDEO','DOCUMENT')),
    url         VARCHAR(1000) NOT NULL,
    sort_order  INT NOT NULL DEFAULT 0,
    alt_text    VARCHAR(255),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_product_media_pid ON product_media (product_id, sort_order);

CREATE TABLE category_attributes (
    category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    attr_key    VARCHAR(60) NOT NULL,
    data_type   VARCHAR(20) NOT NULL CHECK (data_type IN ('STRING','NUMBER','BOOL','ENUM')),
    is_required BOOLEAN NOT NULL DEFAULT FALSE,
    is_facet    BOOLEAN NOT NULL DEFAULT FALSE,
    enum_values JSONB,
    PRIMARY KEY (category_id, attr_key)
);

CREATE TABLE product_outbox (
    id           BIGSERIAL PRIMARY KEY,
    aggregate_id UUID NOT NULL,
    event_type   VARCHAR(60) NOT NULL,
    payload      JSONB NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    sent_at      TIMESTAMPTZ
);
CREATE INDEX idx_prod_outbox_pending ON product_outbox (status, created_at) WHERE status = 'PENDING';
