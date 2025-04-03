import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes } from "@fortawesome/free-solid-svg-icons";

export default function ImageModal({
  isOpen,
  onClose,
  imageSrc,
  altText,
}: {
  isOpen: boolean;
  onClose: () => void;
  imageSrc: string;
  altText: string;
}) {
  if (!isOpen) return null;

  return (
    <div className='fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center z-50'>
      <div className='relative'>
        <img src={imageSrc} alt={altText} className='max-w-full max-h-screen' />
        <button
          className='absolute top-4 right-4 bg-white text-gray-700 p-2 rounded-full shadow hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-300'
          onClick={onClose}>
          <FontAwesomeIcon icon={faTimes} size='lg' />
        </button>
      </div>
    </div>
  );
}
