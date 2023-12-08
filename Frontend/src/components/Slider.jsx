import "bootstrap/dist/css/bootstrap.min.css";
import {
  MDBCarousel,
  MDBCarouselItem,
  MDBCarouselCaption,
} from "mdb-react-ui-kit";

const Slider = () => {
  return (
    <MDBCarousel showIndicators showControls fade>
      <MDBCarouselItem itemId={1}>
        <img
          src="src\assets\image\carousel-1.jpg"
          className="d-block w-100"
          alt="..."
        />
        <MDBCarouselCaption>
          <h5>First slide label</h5>
          <p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
        </MDBCarouselCaption>
      </MDBCarouselItem>

      <MDBCarouselItem itemId={2}>
        <img
          src="src/assets/image/carousel-2.jpg"
          className="d-block w-100"
          alt="..."
        />
        <MDBCarouselCaption>
          <h5>Second slide label</h5>
          <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
        </MDBCarouselCaption>
      </MDBCarouselItem>

      <MDBCarouselItem itemId={3}>
        <img
          src="src/assets/image/carousel-2.jpg"
          className="d-block w-100"
          alt="..."
        />
        <MDBCarouselCaption>
          <h5>Third slide label</h5>
          <p>
            Praesent commodo cursus magna, vel scelerisque nisl consectetur.
          </p>
        </MDBCarouselCaption>
      </MDBCarouselItem>
    </MDBCarousel>
  );
};

export default Slider;
