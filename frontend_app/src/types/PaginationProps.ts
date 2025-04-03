export interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (direction: "prev" | "next") => void;
}
