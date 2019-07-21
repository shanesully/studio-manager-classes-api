package com.studiomanager.studioapi.domain;

import java.time.LocalDate;

public class Booking {
  int id;
  int classId;
  String memberName;
  LocalDate bookingDate;

  private Booking() {}

  private Booking(int id, int classId, String memberName, LocalDate bookingDate) {
    id = id;
    classId = classId;
    memberName = memberName;
    bookingDate = bookingDate;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getClassId() {
    return classId;
  }

  public void setClassId(int classId) {
    this.classId = classId;
  }

  public String getMemberName() {
    return memberName;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }

  public LocalDate getBookingDate() {
    return bookingDate;
  }

  public void setBookingDate(LocalDate bookingDate) {
    this.bookingDate = bookingDate;
  }

  public static class Builder {
    int id;
    int classId;
    String memberName;
    LocalDate bookingDate;

    public Builder withId(int id) {
      id = id;
      return this;
    }

    public Builder withClassId(int classId) {
      classId = classId;
      return this;
    }

    public Builder withMemberName(String memberName) {
      memberName = memberName;
      return this;
    }

    public Builder withBookingDate(LocalDate bookingDate) {
      bookingDate = bookingDate;
      return this;
    }

    public Booking build() {
      return new Booking(id, classId, memberName, bookingDate);
    }
  }
}