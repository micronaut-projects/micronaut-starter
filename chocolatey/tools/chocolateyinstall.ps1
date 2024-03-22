$version = '4.3.7'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'D27FF7FED227A459B99833DEDE313B11B2507051966AEA93CBEF3AB5DA74955B'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
