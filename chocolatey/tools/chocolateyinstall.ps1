$version = '4.5.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '50190CDE8C7613BFD8FD3D821330C016DBD3A7BCD29DE9323684058E626E4409'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
