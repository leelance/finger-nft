package com.fingerchar.db.dto;

import lombok.Data;

/**
 * NftMetadata
 *
 * @author Zjm
 * @since 2024/10/19 16:22
 */
@Data
public class NftMetadata {

  private String address;

  private String tokenId;

  private String name;

  private String description;
  /**
   * nft 存储json id
   */
  private Long storageId;

  private String animUrl;

  private Long animStorageId;

  private String properties;

  private String metadataUrl;

  private String metadataContent;

  public NftMetadata(NftInfo nftInfo) {
    this.address = nftInfo.getAddress();
    this.tokenId = nftInfo.getTokenId();
    this.name = nftInfo.getName();
    this.description = nftInfo.getDescription();
    this.storageId = nftInfo.getStorageId();
    this.animUrl = nftInfo.getAnimUrl();
    this.animStorageId = nftInfo.getAnimStorageId();
    this.properties = nftInfo.getProperties();
    this.metadataUrl = nftInfo.getMetadataUrl();
    this.metadataContent = nftInfo.getMetadataContent();
  }
}
