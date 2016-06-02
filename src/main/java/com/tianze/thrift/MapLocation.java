/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.tianze.thrift;

import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;
import org.apache.thrift.scheme.TupleScheme;

import javax.annotation.Generated;
import java.util.*;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-1-22")
public class MapLocation implements org.apache.thrift.TBase<MapLocation, MapLocation._Fields>, java.io.Serializable, Cloneable, Comparable<MapLocation> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("MapLocation");

  private static final org.apache.thrift.protocol.TField LONGTITUDE_FIELD_DESC = new org.apache.thrift.protocol.TField("longtitude", org.apache.thrift.protocol.TType.DOUBLE, (short)1);
  private static final org.apache.thrift.protocol.TField LATITUDE_FIELD_DESC = new org.apache.thrift.protocol.TField("latitude", org.apache.thrift.protocol.TType.DOUBLE, (short)2);
  private static final org.apache.thrift.protocol.TField COUNTRY_FIELD_DESC = new org.apache.thrift.protocol.TField("country", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField PROVINCE_FIELD_DESC = new org.apache.thrift.protocol.TField("province", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField CITY_FIELD_DESC = new org.apache.thrift.protocol.TField("city", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField TOWN_FIELD_DESC = new org.apache.thrift.protocol.TField("town", org.apache.thrift.protocol.TType.STRING, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new MapLocationStandardSchemeFactory());
    schemes.put(TupleScheme.class, new MapLocationTupleSchemeFactory());
  }

  public double longtitude; // required
  public double latitude; // required
  public String country; // optional
  public String province; // optional
  public String city; // optional
  public String town; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    LONGTITUDE((short)1, "longtitude"),
    LATITUDE((short)2, "latitude"),
    COUNTRY((short)3, "country"),
    PROVINCE((short)4, "province"),
    CITY((short)5, "city"),
    TOWN((short)6, "town");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // LONGTITUDE
          return LONGTITUDE;
        case 2: // LATITUDE
          return LATITUDE;
        case 3: // COUNTRY
          return COUNTRY;
        case 4: // PROVINCE
          return PROVINCE;
        case 5: // CITY
          return CITY;
        case 6: // TOWN
          return TOWN;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __LONGTITUDE_ISSET_ID = 0;
  private static final int __LATITUDE_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.COUNTRY, _Fields.PROVINCE, _Fields.CITY, _Fields.TOWN};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.LONGTITUDE, new org.apache.thrift.meta_data.FieldMetaData("longtitude", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    tmpMap.put(_Fields.LATITUDE, new org.apache.thrift.meta_data.FieldMetaData("latitude", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    tmpMap.put(_Fields.COUNTRY, new org.apache.thrift.meta_data.FieldMetaData("country", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PROVINCE, new org.apache.thrift.meta_data.FieldMetaData("province", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CITY, new org.apache.thrift.meta_data.FieldMetaData("city", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.TOWN, new org.apache.thrift.meta_data.FieldMetaData("town", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(MapLocation.class, metaDataMap);
  }

  public MapLocation() {
  }

  public MapLocation(
    double longtitude,
    double latitude)
  {
    this();
    this.longtitude = longtitude;
    setLongtitudeIsSet(true);
    this.latitude = latitude;
    setLatitudeIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public MapLocation(MapLocation other) {
    __isset_bitfield = other.__isset_bitfield;
    this.longtitude = other.longtitude;
    this.latitude = other.latitude;
    if (other.isSetCountry()) {
      this.country = other.country;
    }
    if (other.isSetProvince()) {
      this.province = other.province;
    }
    if (other.isSetCity()) {
      this.city = other.city;
    }
    if (other.isSetTown()) {
      this.town = other.town;
    }
  }

  public MapLocation deepCopy() {
    return new MapLocation(this);
  }

  public void clear() {
    setLongtitudeIsSet(false);
    this.longtitude = 0.0;
    setLatitudeIsSet(false);
    this.latitude = 0.0;
    this.country = null;
    this.province = null;
    this.city = null;
    this.town = null;
  }

  public double getLongtitude() {
    return this.longtitude;
  }

  public MapLocation setLongtitude(double longtitude) {
    this.longtitude = longtitude;
    setLongtitudeIsSet(true);
    return this;
  }

  public void unsetLongtitude() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __LONGTITUDE_ISSET_ID);
  }

  /** Returns true if field longtitude is set (has been assigned a value) and false otherwise */
  public boolean isSetLongtitude() {
    return EncodingUtils.testBit(__isset_bitfield, __LONGTITUDE_ISSET_ID);
  }

  public void setLongtitudeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __LONGTITUDE_ISSET_ID, value);
  }

  public double getLatitude() {
    return this.latitude;
  }

  public MapLocation setLatitude(double latitude) {
    this.latitude = latitude;
    setLatitudeIsSet(true);
    return this;
  }

  public void unsetLatitude() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __LATITUDE_ISSET_ID);
  }

  /** Returns true if field latitude is set (has been assigned a value) and false otherwise */
  public boolean isSetLatitude() {
    return EncodingUtils.testBit(__isset_bitfield, __LATITUDE_ISSET_ID);
  }

  public void setLatitudeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __LATITUDE_ISSET_ID, value);
  }

  public String getCountry() {
    return this.country;
  }

  public MapLocation setCountry(String country) {
    this.country = country;
    return this;
  }

  public void unsetCountry() {
    this.country = null;
  }

  /** Returns true if field country is set (has been assigned a value) and false otherwise */
  public boolean isSetCountry() {
    return this.country != null;
  }

  public void setCountryIsSet(boolean value) {
    if (!value) {
      this.country = null;
    }
  }

  public String getProvince() {
    return this.province;
  }

  public MapLocation setProvince(String province) {
    this.province = province;
    return this;
  }

  public void unsetProvince() {
    this.province = null;
  }

  /** Returns true if field province is set (has been assigned a value) and false otherwise */
  public boolean isSetProvince() {
    return this.province != null;
  }

  public void setProvinceIsSet(boolean value) {
    if (!value) {
      this.province = null;
    }
  }

  public String getCity() {
    return this.city;
  }

  public MapLocation setCity(String city) {
    this.city = city;
    return this;
  }

  public void unsetCity() {
    this.city = null;
  }

  /** Returns true if field city is set (has been assigned a value) and false otherwise */
  public boolean isSetCity() {
    return this.city != null;
  }

  public void setCityIsSet(boolean value) {
    if (!value) {
      this.city = null;
    }
  }

  public String getTown() {
    return this.town;
  }

  public MapLocation setTown(String town) {
    this.town = town;
    return this;
  }

  public void unsetTown() {
    this.town = null;
  }

  /** Returns true if field town is set (has been assigned a value) and false otherwise */
  public boolean isSetTown() {
    return this.town != null;
  }

  public void setTownIsSet(boolean value) {
    if (!value) {
      this.town = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case LONGTITUDE:
      if (value == null) {
        unsetLongtitude();
      } else {
        setLongtitude((Double)value);
      }
      break;

    case LATITUDE:
      if (value == null) {
        unsetLatitude();
      } else {
        setLatitude((Double)value);
      }
      break;

    case COUNTRY:
      if (value == null) {
        unsetCountry();
      } else {
        setCountry((String)value);
      }
      break;

    case PROVINCE:
      if (value == null) {
        unsetProvince();
      } else {
        setProvince((String)value);
      }
      break;

    case CITY:
      if (value == null) {
        unsetCity();
      } else {
        setCity((String)value);
      }
      break;

    case TOWN:
      if (value == null) {
        unsetTown();
      } else {
        setTown((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case LONGTITUDE:
      return Double.valueOf(getLongtitude());

    case LATITUDE:
      return Double.valueOf(getLatitude());

    case COUNTRY:
      return getCountry();

    case PROVINCE:
      return getProvince();

    case CITY:
      return getCity();

    case TOWN:
      return getTown();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case LONGTITUDE:
      return isSetLongtitude();
    case LATITUDE:
      return isSetLatitude();
    case COUNTRY:
      return isSetCountry();
    case PROVINCE:
      return isSetProvince();
    case CITY:
      return isSetCity();
    case TOWN:
      return isSetTown();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof MapLocation)
      return this.equals((MapLocation)that);
    return false;
  }

  public boolean equals(MapLocation that) {
    if (that == null)
      return false;

    boolean this_present_longtitude = true;
    boolean that_present_longtitude = true;
    if (this_present_longtitude || that_present_longtitude) {
      if (!(this_present_longtitude && that_present_longtitude))
        return false;
      if (this.longtitude != that.longtitude)
        return false;
    }

    boolean this_present_latitude = true;
    boolean that_present_latitude = true;
    if (this_present_latitude || that_present_latitude) {
      if (!(this_present_latitude && that_present_latitude))
        return false;
      if (this.latitude != that.latitude)
        return false;
    }

    boolean this_present_country = true && this.isSetCountry();
    boolean that_present_country = true && that.isSetCountry();
    if (this_present_country || that_present_country) {
      if (!(this_present_country && that_present_country))
        return false;
      if (!this.country.equals(that.country))
        return false;
    }

    boolean this_present_province = true && this.isSetProvince();
    boolean that_present_province = true && that.isSetProvince();
    if (this_present_province || that_present_province) {
      if (!(this_present_province && that_present_province))
        return false;
      if (!this.province.equals(that.province))
        return false;
    }

    boolean this_present_city = true && this.isSetCity();
    boolean that_present_city = true && that.isSetCity();
    if (this_present_city || that_present_city) {
      if (!(this_present_city && that_present_city))
        return false;
      if (!this.city.equals(that.city))
        return false;
    }

    boolean this_present_town = true && this.isSetTown();
    boolean that_present_town = true && that.isSetTown();
    if (this_present_town || that_present_town) {
      if (!(this_present_town && that_present_town))
        return false;
      if (!this.town.equals(that.town))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_longtitude = true;
    list.add(present_longtitude);
    if (present_longtitude)
      list.add(longtitude);

    boolean present_latitude = true;
    list.add(present_latitude);
    if (present_latitude)
      list.add(latitude);

    boolean present_country = true && (isSetCountry());
    list.add(present_country);
    if (present_country)
      list.add(country);

    boolean present_province = true && (isSetProvince());
    list.add(present_province);
    if (present_province)
      list.add(province);

    boolean present_city = true && (isSetCity());
    list.add(present_city);
    if (present_city)
      list.add(city);

    boolean present_town = true && (isSetTown());
    list.add(present_town);
    if (present_town)
      list.add(town);

    return list.hashCode();
  }

  @Override
  public int compareTo(MapLocation other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetLongtitude()).compareTo(other.isSetLongtitude());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLongtitude()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.longtitude, other.longtitude);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetLatitude()).compareTo(other.isSetLatitude());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLatitude()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.latitude, other.latitude);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCountry()).compareTo(other.isSetCountry());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCountry()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.country, other.country);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetProvince()).compareTo(other.isSetProvince());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetProvince()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.province, other.province);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCity()).compareTo(other.isSetCity());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCity()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.city, other.city);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTown()).compareTo(other.isSetTown());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTown()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.town, other.town);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("MapLocation(");
    boolean first = true;

    sb.append("longtitude:");
    sb.append(this.longtitude);
    first = false;
    if (!first) sb.append(", ");
    sb.append("latitude:");
    sb.append(this.latitude);
    first = false;
    if (isSetCountry()) {
      if (!first) sb.append(", ");
      sb.append("country:");
      if (this.country == null) {
        sb.append("null");
      } else {
        sb.append(this.country);
      }
      first = false;
    }
    if (isSetProvince()) {
      if (!first) sb.append(", ");
      sb.append("province:");
      if (this.province == null) {
        sb.append("null");
      } else {
        sb.append(this.province);
      }
      first = false;
    }
    if (isSetCity()) {
      if (!first) sb.append(", ");
      sb.append("city:");
      if (this.city == null) {
        sb.append("null");
      } else {
        sb.append(this.city);
      }
      first = false;
    }
    if (isSetTown()) {
      if (!first) sb.append(", ");
      sb.append("town:");
      if (this.town == null) {
        sb.append("null");
      } else {
        sb.append(this.town);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
    // alas, we cannot check 'longtitude' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'latitude' because it's a primitive and you chose the non-beans generator.
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (TException te) {
      throw new java.io.IOException(te.getMessage());
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (TException te) {
      throw new java.io.IOException(te.getMessage());
    }
  }

  private static class MapLocationStandardSchemeFactory implements SchemeFactory {
    public MapLocationStandardScheme getScheme() {
      return new MapLocationStandardScheme();
    }
  }

  private static class MapLocationStandardScheme extends StandardScheme<MapLocation> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, MapLocation struct) throws TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
          break;
        }
        switch (schemeField.id) {
          case 1: // LONGTITUDE
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.longtitude = iprot.readDouble();
              struct.setLongtitudeIsSet(true);
            } else {
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // LATITUDE
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.latitude = iprot.readDouble();
              struct.setLatitudeIsSet(true);
            } else {
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // COUNTRY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.country = iprot.readString();
              struct.setCountryIsSet(true);
            } else {
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // PROVINCE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.province = iprot.readString();
              struct.setProvinceIsSet(true);
            } else {
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // CITY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.city = iprot.readString();
              struct.setCityIsSet(true);
            } else {
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // TOWN
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.town = iprot.readString();
              struct.setTownIsSet(true);
            } else {
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      if (!struct.isSetLongtitude()) {
        throw new TProtocolException("Required field 'longtitude' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetLatitude()) {
        throw new TProtocolException("Required field 'latitude' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, MapLocation struct) throws TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(LONGTITUDE_FIELD_DESC);
      oprot.writeDouble(struct.longtitude);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(LATITUDE_FIELD_DESC);
      oprot.writeDouble(struct.latitude);
      oprot.writeFieldEnd();
      if (struct.country != null) {
        if (struct.isSetCountry()) {
          oprot.writeFieldBegin(COUNTRY_FIELD_DESC);
          oprot.writeString(struct.country);
          oprot.writeFieldEnd();
        }
      }
      if (struct.province != null) {
        if (struct.isSetProvince()) {
          oprot.writeFieldBegin(PROVINCE_FIELD_DESC);
          oprot.writeString(struct.province);
          oprot.writeFieldEnd();
        }
      }
      if (struct.city != null) {
        if (struct.isSetCity()) {
          oprot.writeFieldBegin(CITY_FIELD_DESC);
          oprot.writeString(struct.city);
          oprot.writeFieldEnd();
        }
      }
      if (struct.town != null) {
        if (struct.isSetTown()) {
          oprot.writeFieldBegin(TOWN_FIELD_DESC);
          oprot.writeString(struct.town);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class MapLocationTupleSchemeFactory implements SchemeFactory {
    public MapLocationTupleScheme getScheme() {
      return new MapLocationTupleScheme();
    }
  }

  private static class MapLocationTupleScheme extends TupleScheme<MapLocation> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, MapLocation struct) throws TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeDouble(struct.longtitude);
      oprot.writeDouble(struct.latitude);
      BitSet optionals = new BitSet();
      if (struct.isSetCountry()) {
        optionals.set(0);
      }
      if (struct.isSetProvince()) {
        optionals.set(1);
      }
      if (struct.isSetCity()) {
        optionals.set(2);
      }
      if (struct.isSetTown()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetCountry()) {
        oprot.writeString(struct.country);
      }
      if (struct.isSetProvince()) {
        oprot.writeString(struct.province);
      }
      if (struct.isSetCity()) {
        oprot.writeString(struct.city);
      }
      if (struct.isSetTown()) {
        oprot.writeString(struct.town);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, MapLocation struct) throws TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.longtitude = iprot.readDouble();
      struct.setLongtitudeIsSet(true);
      struct.latitude = iprot.readDouble();
      struct.setLatitudeIsSet(true);
      BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.country = iprot.readString();
        struct.setCountryIsSet(true);
      }
      if (incoming.get(1)) {
        struct.province = iprot.readString();
        struct.setProvinceIsSet(true);
      }
      if (incoming.get(2)) {
        struct.city = iprot.readString();
        struct.setCityIsSet(true);
      }
      if (incoming.get(3)) {
        struct.town = iprot.readString();
        struct.setTownIsSet(true);
      }
    }
  }

}

