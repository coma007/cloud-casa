import { useEffect, useState } from "react";
import { RequestService } from "../RequestService";
import { RealEstate } from "../../estate/RealEstate";
import OverviewTable from "./OverviewTable";

const EstateOverview = () => {

  const [estates, setEstates] = useState<RealEstate[]>();

  useEffect(() => {
    RequestService.getAll().then((value) => { setEstates(value); console.log(value) })
  }, [])

  return (
    <div>
      <h1>Real Estates Requests Overview</h1>
      <OverviewTable items={estates} />
    </div>
  );
};

export default EstateOverview;
