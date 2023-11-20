import { useEffect, useState } from "react";

const EstateOverview = () => {

  const [estates, setEstates] = useState<RealEstate[]>();

  useEffect(() => {
    EstateService.getAllByOwner().then((value) => setEstates(value))
  }, [])

  return (
    <div>
      <h1>My Real Estates</h1>
      <OverviewTable items={estates} />
    </div>
  );
};

export default EstateOverview;
