package utils

import "time"

type AirConditioningSchedule struct {
	id int64

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
	if min.After(max) {
		min, max = max, min
	}
	return (t.Equal(min) || t.After(min)) && (t.Equal(max) || t.Before(max))
}
