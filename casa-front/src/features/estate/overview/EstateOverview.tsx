import { useEffect, useState } from "react";
import { RealEstate, RealEstateCreate } from "../RealEstate";
import OverviewTable from "./OverviewTable";
import { EstateService } from "../EstateService";
import Menu from "../../../components/navigation/Menu/Menu";

const EstateOverview = () => {

  const [estates, setEstates] = useState<RealEstate[]>();

  useEffect(() => {
    EstateService.getAllByOwner().then((value) => { setEstates(value); console.log(value) })
  }, [])

  return (
    <div>
      <Menu admin={false}/>
      <h1>My Real Estates</h1>
      <OverviewTable items={estates} />
    </div>
  );
};

export default EstateOverview;
