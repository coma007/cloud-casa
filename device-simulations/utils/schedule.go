package utils

import "time"

type AirConditioningSchedule struct {
	Id int64 `json:"id"`

	StartTime CustomTime `json:"startTime"`
	EndTime   CustomTime `json:"endTime"`

	Override  bool `json:"override"`
	Activated bool `json:"activated"`

	Working     bool     `json:"working"`
	Mode        *string  `json:"mode"`
	Temperature *float64 `json:"temperature"`

	Repeating              bool  `json:"repeating"`
	RepeatingDaysIncrement int64 `json:"repeatingDaysIncrement"`
}

type WashingMachineSchedule struct {
	Id int64 `json:"id"`

	StartTime CustomTime `json:"startTime"`
	EndTime   CustomTime `json:"endTime"`

	Activated bool `json:"activated"`

	Working bool    `json:"working"`
	Mode    *string `json:"mode"`
}

func (schedule *AirConditioningSchedule) FixTimezone() {
	loc := GetTimezoneLocation()
	schedule.StartTime.Time = time.Date(schedule.StartTime.Time.Year(),
		schedule.StartTime.Time.Month(), schedule.StartTime.Time.Day(),
		schedule.StartTime.Time.Hour(), schedule.StartTime.Time.Minute(),
		schedule.StartTime.Time.Second(), schedule.StartTime.Time.Nanosecond(), loc)
	schedule.EndTime.Time = time.Date(schedule.EndTime.Time.Year(),
		schedule.EndTime.Time.Month(), schedule.EndTime.Time.Day(),
		schedule.EndTime.Time.Hour(), schedule.EndTime.Time.Minute(),
		schedule.EndTime.Time.Second(), schedule.EndTime.Time.Nanosecond(), loc)
}

func (schedule *WashingMachineSchedule) FixTimezone() {
	loc := GetTimezoneLocation()
	schedule.StartTime.Time = time.Date(schedule.StartTime.Time.Year(),
		schedule.StartTime.Time.Month(), schedule.StartTime.Time.Day(),
		schedule.StartTime.Time.Hour(), schedule.StartTime.Time.Minute(),
		schedule.StartTime.Time.Second(), schedule.StartTime.Time.Nanosecond(), loc)
	schedule.EndTime.Time = time.Date(schedule.EndTime.Time.Year(),
		schedule.EndTime.Time.Month(), schedule.EndTime.Time.Day(),
		schedule.EndTime.Time.Hour(), schedule.EndTime.Time.Minute(),
		schedule.EndTime.Time.Second(), schedule.EndTime.Time.Nanosecond(), loc)
}

type CustomTime struct {
	time.Time
}

func (t *CustomTime) UnmarshalJSON(b []byte) (err error) {
	date, err := time.Parse(`"2006-01-02T15:04:05"`, string(b))
	if err != nil {
		return err
	}
	t.Time = date
	//loc, err := time.LoadLocation("Europe/Belgrade")
	//t.Time = t.Time.In(loc)
	return
}

func TimeIsBetween(t, min, max time.Time) bool {
	return (t.Equal(min) || t.After(min)) && (t.Equal(max) || t.Before(max))
}
