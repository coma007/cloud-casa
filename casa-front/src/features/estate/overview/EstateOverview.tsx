import { Row, Col } from 'react-bootstrap';
import RegisterForm from '../register/RegisterForm';
import SimpleMap from '../register/Map';

const EstateOverview = () => {
  return (
    <div className="container">
      <h2 className="mb-4">Register Real Estate</h2>
      <Row>
        <Col md={6}>
          <div>
            <RegisterForm onSubmit={() => console.log("submit")} />
          </div>
        </Col>
        <Col md={6}>
          <SimpleMap></SimpleMap>
        </Col>
      </Row>
    </div>
  );
};

export default EstateOverview;
