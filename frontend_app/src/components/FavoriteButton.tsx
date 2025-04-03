import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faHeart } from "@fortawesome/free-solid-svg-icons";

export default function FavoriteButton() {
  return (
    <button
      className='absolute top-4 right-4 bg-white text-red-500 p-2 rounded-full shadow hover:bg-red-100 focus:outline-none focus:ring-2 focus:ring-red-300'
      onClick={() => alert("Dodano do ulubionych!")}>
      <FontAwesomeIcon icon={faHeart} size='lg' />
    </button>
  );
}
