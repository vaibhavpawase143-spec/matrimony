import DOMPurify from "dompurify";

const CMSContent = ({ html }) => {
  return (
    <div className="container mx-auto px-4 py-12">
      <div
        className="cms-content max-w-5xl mx-auto bg-white rounded-2xl border border-border shadow-sm p-8 md:p-10"
        dangerouslySetInnerHTML={{
          __html: DOMPurify.sanitize(html || ""),
        }}
      />
    </div>
  );
};

export default CMSContent;