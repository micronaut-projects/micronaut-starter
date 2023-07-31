$version = '3.10.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '8AB8201570D6C53FE548DFB60EFD0579AAED11F343210AF8DD593A9A28DC7706'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
